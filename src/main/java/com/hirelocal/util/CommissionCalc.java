package com.hirelocal.util;

public class CommissionCalc {

    public static double calculate(double amount) {
        if (amount <= 500) return amount * 0.10;
        else if (amount <= 1000) return amount * 0.12;
        else if (amount <= 2000) return amount * 0.15;
        else return amount * 0.18;
    }

    public static double workerEarning(double amount) {
        return amount - calculate(amount);
    }
}