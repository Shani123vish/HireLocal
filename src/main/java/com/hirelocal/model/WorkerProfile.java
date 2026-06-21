package com.hirelocal.model;

public class WorkerProfile {
    private int id;
    private int userId;
    private int skillCategoryId;
    private double basePrice;
    private String serviceArea;
    private int experienceYears;
    private boolean isVerified;
    private boolean isAvailable;
    private double rating;
    private int totalJobs;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getSkillCategoryId() { return skillCategoryId; }
    public void setSkillCategoryId(int skillCategoryId) { this.skillCategoryId = skillCategoryId; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getTotalJobs() { return totalJobs; }
    public void setTotalJobs(int totalJobs) { this.totalJobs = totalJobs; }
}