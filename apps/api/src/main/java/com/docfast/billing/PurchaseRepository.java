package com.docfast.billing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByMpPreferenceId(String mpPreferenceId);

    Optional<Purchase> findByMpPaymentId(String mpPaymentId);
}
