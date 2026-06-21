package com.hirelocal.model;

public class Job {
    private int id;
    private int customerId;
    private int categoryId;
    private String description;
    private String photoPath;
    private String address;
    private int cityId;
    private String scheduledTime;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}