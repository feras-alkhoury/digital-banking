package com.nexvia.digital_banking;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/kyc")
public class KycController {

    private final KycRepository repository;

    public KycController(KycRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Kyc submit(@RequestParam Long customerId) {

        Kyc kyc = new Kyc();
        kyc.setCustomerId(customerId);
        kyc.setStatus("PENDING");
        kyc.setSubmittedAt(LocalDateTime.now());

        return repository.save(kyc);
    }

    @GetMapping("/{id}")
    public Kyc get(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("KYC not found"));
    }

    @PutMapping("/{id}/approve")
    public Kyc approve(@PathVariable Long id) {
        Kyc kyc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("KYC not found"));

        kyc.setStatus("APPROVED");
        return repository.save(kyc);
    }
}