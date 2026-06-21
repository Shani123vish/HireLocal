package com.hirelocal.model;

public class Escrow {
    private int id;
    private int jobId;
    private int customerId;
    private int workerId;
    private double amount;
    private double commission;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getWorkerId() { return workerId; }
    public void setWorkerId(int workerId) { this.workerId = workerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getCommission() { return commission; }
    public void setCommission(double commission) { this.commission = commission; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}