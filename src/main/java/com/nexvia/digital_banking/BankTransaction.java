package com.nexvia.digital_banking;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", unique = true, nullable = false)
    private String idempotencyKey;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String status;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public String getIdempotencyKey() {
         return idempotencyKey; 
        }
    public void setIdempotencyKey(String key) {
    this.idempotencyKey = key;
}

    public void setIdempotencyKey1(String key) {
    this.idempotencyKey = key;
}

public void setFromAccount(Account account) {
    this.fromAccount = account;
}

public void setToAccount(Account account) {
    this.toAccount = account;
}

public void setAmount(BigDecimal amount) {
    this.amount = amount;
}

public void setStatus(String status) {
    this.status = status;
}

public String getStatus() {
    return status;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}
}
