package com.example.Slot_Machine.model;
import java.util.List;


public class SpinResult {
    public List<String> getReels() {
        return reels;
    }

    public void setReels(List<String> reels) {
        this.reels = reels;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    private List<String> reels;
    private int payout;
    private int balance;
    private boolean win;

    public SpinResult(List<String> reels, int payout, int balance, boolean win) {
        this.reels = reels;
        this.payout = payout;
        this.balance = balance;
        this.win = win;
    }
// Constructors, getters, setters
}
