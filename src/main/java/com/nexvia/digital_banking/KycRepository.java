package com.nexvia.digital_banking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KycRepository extends JpaRepository<Kyc, Long> {
}