package com.hirelocal.model;

public class Cancellation {
    private int id;
    private int customerId;
    private int jobId;
    private int cancelCount;
    private double penaltyAmount;
    private boolean isBlocked;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public int getCancelCount() { return cancelCount; }
    public void setCancelCount(int cancelCount) { this.cancelCount = cancelCount; }
    public double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(double penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
}