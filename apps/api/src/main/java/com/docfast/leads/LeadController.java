package com.docfast.leads;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private static final Logger log = LoggerFactory.getLogger(LeadController.class);

    private final LeadRepository repository;

    public LeadController(LeadRepository repository) {
        this.repository = repository;
    }

    public record LeadRequest(
            @NotBlank @Email @Size(max = 255) String email,
            @Size(max = 50) String source) {
    }

    public record LeadResponse(boolean ok) {
    }

    @PostMapping
    public LeadResponse capture(@Valid @RequestBody LeadRequest request) {
        var email = request.email().trim().toLowerCase();
        var source = request.source() == null ? "smart-gate" : request.source();
        repository.findByEmail(email).orElseGet(() -> {
            log.info("Lead capturado: source={}", source);
            return repository.save(new Lead(email, source));
        });
        return new LeadResponse(true);
    }
}
