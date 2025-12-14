package org.example;

public class User {
    private int userId;
    private int balance;

    public User(int userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean hasEnoughBalance(int amount) {
        return this.balance >= amount;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Balance: " + balance;
    }
}