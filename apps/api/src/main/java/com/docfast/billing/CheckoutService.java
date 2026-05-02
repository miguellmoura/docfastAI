package com.docfast.billing;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final PurchaseRepository repository;
    private final String frontendUrl;

    public CheckoutService(
            PurchaseRepository repository,
            @Value("${app.mercadopago.access-token}") String accessToken,
            @Value("${app.frontend.url}") String frontendUrl) {
        this.repository = repository;
        this.frontendUrl = frontendUrl;
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public record CheckoutResponse(String initPoint, Long purchaseId) {
    }

    public CheckoutResponse createCheckout(String email, Purchase.Plan plan) {
        var planData = getPlanData(plan);
        var purchase = repository.save(new Purchase(email, plan, planData.price, null));

        try {
            var item = PreferenceItemRequest.builder()
                    .title(planData.title)
                    .description(planData.description)
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(planData.price)
                    .build();

            // TODO: Habilitar backUrls quando tiver domínio público (MP rejeita localhost)
            // var backUrls = PreferenceBackUrlsRequest.builder()
            // .success(frontendUrl + "/checkout/sucesso?purchaseId=" + purchase.getId())
            // .failure(frontendUrl + "/checkout/cancelado")
            // .pending(frontendUrl + "/checkout/pendente")
            // .build();

            var request = PreferenceRequest.builder()
                    .items(List.of(item))
                    // .backUrls(backUrls) // Desabilitado - MP rejeita localhost
                    // .autoReturn("approved") // Só funciona com backUrls válidas
                    .externalReference(String.valueOf(purchase.getId()))
                    .notificationUrl("https://webhook.site/unique-id-here") // TODO: trocar por domínio real no deploy
                    .payer(com.mercadopago.client.preference.PreferencePayerRequest.builder()
                            .email(email)
                            .build())
                    .build();

            var client = new PreferenceClient();
            Preference preference = client.create(request);

            purchase = repository.findById(purchase.getId()).orElseThrow();
            var updated = new Purchase(purchase.getEmail(), purchase.getPlan(), purchase.getAmount(),
                    preference.getId());
            updated = repository.save(updated);

            log.info("Preferência criada: preferenceId={}, purchaseId={}", preference.getId(), updated.getId());
            return new CheckoutResponse(preference.getInitPoint(), updated.getId());

        } catch (MPApiException e) {
            var responseContent = e.getApiResponse() != null ? e.getApiResponse().getContent() : "sem resposta";
            log.error("Erro MPApiException - Status: {}, Content: {}", e.getStatusCode(), responseContent, e);
            throw new RuntimeException("Erro ao criar checkout. Tente novamente.");
        } catch (MPException e) {
            log.error("Erro MPException: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar checkout. Tente novamente.");
        }
    }

    private record PlanData(String title, String description, BigDecimal price) {
    }

    private PlanData getPlanData(Purchase.Plan plan) {
        return switch (plan) {
            case LIGHT -> new PlanData(
                    "DocFast AI - Plano Light",
                    "15 análises com IA · Válido por 90 dias",
                    new BigDecimal("9.00"));
            case PRO -> new PlanData(
                    "DocFast AI - Plano Pro",
                    "80 análises/mês · PDFs até 100 páginas",
                    new BigDecimal("19.00"));
            case POWER -> new PlanData(
                    "DocFast AI - Plano Power",
                    "300 análises/mês · PDFs até 200 páginas",
                    new BigDecimal("39.00"));
        };
    }
}
