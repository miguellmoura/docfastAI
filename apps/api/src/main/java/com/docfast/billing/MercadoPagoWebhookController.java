package com.docfast.billing;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@RestController
@RequestMapping("/api/webhooks/mercadopago")
public class MercadoPagoWebhookController {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoWebhookController.class);

    private final PurchaseRepository repository;
    private final String webhookSecret;

    public MercadoPagoWebhookController(
            PurchaseRepository repository,
            @Value("${app.mercadopago.access-token}") String accessToken,
            @Value("${app.mercadopago.webhook-secret:}") String webhookSecret) {
        this.repository = repository;
        this.webhookSecret = webhookSecret;
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestParam(value = "data.id", required = false) String dataId,
            @RequestParam(value = "type", required = false) String type,
            @RequestHeader(value = "x-signature", required = false) String xSignature,
            @RequestHeader(value = "x-request-id", required = false) String xRequestId,
            @RequestBody String rawBody) {

        log.info("Webhook recebido: type={}, dataId={}", type, dataId);

        // Validar HMAC se webhook secret estiver configurado
        if (!webhookSecret.isBlank() && xSignature != null && xRequestId != null) {
            if (!validateSignature(xSignature, xRequestId, dataId, rawBody)) {
                log.warn("Assinatura inválida no webhook. Rejeitado.");
                return ResponseEntity.status(401).build();
            }
        }

        if ("payment".equals(type) && dataId != null) {
            processPayment(Long.parseLong(dataId));
        }

        return ResponseEntity.ok().build();
    }

    private void processPayment(Long paymentId) {
        try {
            var client = new PaymentClient();
            Payment payment = client.get(paymentId);

            log.info("Pagamento recebido: id={}, status={}, externalReference={}",
                    payment.getId(), payment.getStatus(), payment.getExternalReference());

            if (payment.getExternalReference() == null) {
                log.warn("ExternalReference null no payment {}. Ignorado.", paymentId);
                return;
            }

            Long purchaseId = Long.parseLong(payment.getExternalReference());
            var purchaseOpt = repository.findById(purchaseId);

            if (purchaseOpt.isEmpty()) {
                log.warn("Purchase {} não encontrada para payment {}.", purchaseId, paymentId);
                return;
            }

            var purchase = purchaseOpt.get();

            if ("approved".equals(payment.getStatus())) {
                purchase.markAsPaid(String.valueOf(payment.getId()));
                repository.save(purchase);
                log.info("Purchase {} marcada como paga.", purchaseId);
                // TODO: enviar e-mail de confirmação + magic link (quando tiver Resend
                // configurado)
            } else if ("rejected".equals(payment.getStatus()) || "cancelled".equals(payment.getStatus())) {
                purchase.markAsFailed();
                repository.save(purchase);
                log.info("Purchase {} marcada como failed.", purchaseId);
            }

        } catch (MPException | MPApiException e) {
            log.error("Erro ao buscar payment {}: {}", paymentId, e.getMessage(), e);
        } catch (NumberFormatException e) {
            log.error("ExternalReference inválido no payment {}", paymentId, e);
        }
    }

    private boolean validateSignature(String xSignature, String xRequestId, String dataId, String rawBody) {
        try {
            // Extrair ts e hash de x-signature (formato: "ts=123456,v1=abc123...")
            String ts = null, v1 = null;
            for (String part : xSignature.split(",")) {
                String[] kv = part.split("=", 2);
                if (kv.length == 2) {
                    if ("ts".equals(kv[0]))
                        ts = kv[1];
                    if ("v1".equals(kv[0]))
                        v1 = kv[1];
                }
            }

            if (ts == null || v1 == null)
                return false;

            // Criar manifest: id:dataId,request-id:xRequestId,ts:ts
            String manifest = String.format("id:%s;request-id:%s;ts:%s", dataId != null ? dataId : "", xRequestId, ts);

            // HMAC-SHA256
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = hmac.doFinal(manifest.getBytes(StandardCharsets.UTF_8));
            String computed = HexFormat.of().formatHex(hash);

            return computed.equalsIgnoreCase(v1);

        } catch (Exception e) {
            log.error("Erro ao validar assinatura: {}", e.getMessage(), e);
            return false;
        }
    }
}
