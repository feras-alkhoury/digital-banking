package com.nexvia.digital_banking;

import java.math.BigDecimal;

public record TransferEvent(
        Long transactionId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        String status
) {}