package com.example.Slot_Machine.controller;

import com.example.Slot_Machine.model.*;
import com.example.Slot_Machine.repository.UserRepository;
import com.example.Slot_Machine.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SlotController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SlotService slotService;

//    @GetMapping("/wallet/{username}")
//    public int getBalance(@PathVariable String username) {
//        return userRepo.findByUsername(username)
//                .map(u -> u.getWallet().getBalance())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
@GetMapping("/wallet/{username}")
public Map<String, Integer> getBalance(@PathVariable String username) {
    int bal = userRepo.findByUsername(username)
            .map(u -> u.getWallet().getBalance())
            .orElseThrow(() -> new RuntimeException("User not found"));
    return Map.of("balance", bal);   // ✅ JSON: {"balance":828}
}
    @PostMapping("/spin/{username}")
    public SpinResult spin(@PathVariable String username, @RequestParam int stake) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = user.getWallet();
        if (stake > wallet.getBalance()) {
            throw new RuntimeException("Bet exceeds balance");
        }
        SpinResult result = slotService.spin(wallet, stake);
        userRepo.save(user);
        return result;
    }

    @PostMapping("/create/{username}")
    public String createUser(@PathVariable String username) {
        Optional<User> existing = userRepo.findByUsername(username);
        if (existing.isPresent()) return "Loaded existing user";

        Wallet wallet = new Wallet();
        wallet.setBalance(1000);

        User user = new User();
        user.setUsername(username);
        user.setWallet(wallet);

        userRepo.save(user);
        return "New user created";
    }
}
