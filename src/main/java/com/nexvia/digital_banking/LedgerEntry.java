package com.nexvia.digital_banking;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private BankTransaction transaction;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String entryType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private LocalDateTime createdAt;

    public void setTransaction(BankTransaction transaction) {
    this.transaction = transaction;
}

public void setAccount(Account account) {
    this.account = account;
}

public void setEntryType(String entryType) {
    this.entryType = entryType;
}

public void setAmount(BigDecimal amount) {
    this.amount = amount;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public Long getId() {
    return id;
}

public BankTransaction getTransaction() {
    return transaction;
}

public Account getAccount() {
    return account;
}

public String getEntryType() {
    return entryType;
}

public BigDecimal getAmount() {
    return amount;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

}