package com.nexvia.digital_banking;

import java.math.BigDecimal;

public record TransferRequest(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String idempotencyKey
) {}