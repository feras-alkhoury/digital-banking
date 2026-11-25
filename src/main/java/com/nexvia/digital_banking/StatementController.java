package com.nexvia.digital_banking;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class StatementController {

    private final LedgerEntryRepository ledgerRepository;

    public StatementController(LedgerEntryRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @GetMapping("/{accountId}/statement")
    public List<LedgerEntry> getStatement(
            @PathVariable Long accountId) {

        return ledgerRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId);
    }
}