package com.example.Slot_Machine.service;


import com.example.Slot_Machine.model.SpinResult;
import org.springframework.stereotype.Service;
import java.util.*;
import com.example.Slot_Machine.model.Wallet;

@Service
public class SlotService {

    List<String> symbols = Arrays.asList("🍒", "🍋", "🍊", "🌽", "🔔", "💰");

    public SpinResult spin(Wallet wallet, int stake) {
        List<String> reels = Arrays.asList(randomSymbol(), randomSymbol(), randomSymbol());

        wallet.debit(stake);
        int payout = calculatePayout(reels) * stake;

        if (payout > 0) {
            wallet.credit(payout);
        }

        return new SpinResult(reels, payout, wallet.getBalance(), payout > 0);
    }

    private String randomSymbol() {
        return symbols.get((int)(Math.random() * symbols.size()));
    }

    private int calculatePayout(List<String> reels) {
        Set<String> unique = new HashSet<>(reels);
        if (unique.size() == 1) {
            String symbol = reels.get(0);
            switch (symbol) {
                case "💰":  return 500;
                case "🔔":    return 100;
                case "🌽":   return 50;
                case "🍊":   return 20;
                case "🍋": return 10;
                case "🍒":  return 5;
                default:       return 0;
            }

        }
        long cherryCount = reels.stream().filter(s -> s.equals("🍒")).count();
        return (cherryCount == 2) ? 1 : 0;
    }
}
