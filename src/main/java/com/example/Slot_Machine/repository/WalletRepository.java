package com.example.Slot_Machine.repository;

import com.example.Slot_Machine.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
