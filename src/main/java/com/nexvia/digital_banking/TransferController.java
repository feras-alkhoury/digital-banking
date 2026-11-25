package com.nexvia.digital_banking;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public String createTransfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}