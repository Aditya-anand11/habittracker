package com.tracker.model;

public abstract class BaseItem {
    private Long id;
    private String title;
    private String priority; // HIGH, MEDIUM, LOW

    public BaseItem() {}

    public BaseItem(Long id, String title, String priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public abstract int getPriorityWeight();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}