package com.nexvia.digital_banking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerRepository;
    private final TransferEventProducer eventProducer;

    public TransferService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            LedgerEntryRepository ledgerRepository,
            TransferEventProducer eventProducer) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public String transfer(TransferRequest request) {

        if (request.amount() == null ||
                request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new IllegalArgumentException("Same account");
        }

        if (request.idempotencyKey() == null ||
                request.idempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Idempotency key required");
        }

        var existing = transactionRepository
                .findByIdempotencyKey(request.idempotencyKey());

        if (existing.isPresent()) {
            return "Transfer already processed";
        }

        if (request.amount().compareTo(new BigDecimal("5000")) > 0) {
            throw new IllegalArgumentException(
                    "Fraud rule: transfer limit exceeded");
        }

        if (transactionRepository.countByFromAccount_Id(
                request.fromAccountId()) >= 3) {
            throw new IllegalArgumentException(
                    "Fraud rule: transfer velocity exceeded");
        }

        Account sender = accountRepository
                .findById(request.fromAccountId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Sender not found"));

        Account receiver = accountRepository
                .findById(request.toAccountId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Receiver not found"));

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.amount()));
        receiver.setBalance(receiver.getBalance().add(request.amount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        BankTransaction transaction = new BankTransaction();

        transaction.setIdempotencyKey(request.idempotencyKey());
        transaction.setFromAccount(sender);
        transaction.setToAccount(receiver);
        transaction.setAmount(request.amount());
        transaction.setStatus("COMPLETED");
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        LedgerEntry debit = new LedgerEntry();
        debit.setTransaction(transaction);
        debit.setAccount(sender);
        debit.setEntryType("DEBIT");
        debit.setAmount(request.amount().negate());
        debit.setCreatedAt(LocalDateTime.now());
        ledgerRepository.save(debit);

        LedgerEntry credit = new LedgerEntry();
        credit.setTransaction(transaction);
        credit.setAccount(receiver);
        credit.setEntryType("CREDIT");
        credit.setAmount(request.amount());
        credit.setCreatedAt(LocalDateTime.now());
        ledgerRepository.save(credit);

        // Publish completed transfer event to Kafka
        eventProducer.publish(new TransferEvent(
                transaction.getId(),
                sender.getId(),
                receiver.getId(),
                request.amount(),
                "COMPLETED"
        ));

        return "Transfer successful";
    }
}