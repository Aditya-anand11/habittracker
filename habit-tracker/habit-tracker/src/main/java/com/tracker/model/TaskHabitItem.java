package com.tracker.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TaskHabitItem extends BaseItem {
    private String category; // TASK or HABIT
    private String frequency; // ONCE, DAILY, WEEKLY
    private LocalDate deadline;
    private boolean completed;
    private int streak;
    private LocalDate lastCompletedDate;

    public TaskHabitItem() {
        super();
    }

    @Override
    public int getPriorityWeight() {
        if ("HIGH".equalsIgnoreCase(getPriority())) return 1;
        if ("MEDIUM".equalsIgnoreCase(getPriority())) return 2;
        return 3;
    }

    // Java Date-Time API: Dynamic Overdue Alert Calculation
    public boolean isOverdue() {
        return !completed && deadline != null && deadline.isBefore(LocalDate.now());
    }

    // Java Date-Time API: Days remaining or overdue
    public long getDaysUntilDeadline() {
        if (deadline == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

    // Getters & Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }
    public LocalDate getLastCompletedDate() { return lastCompletedDate; }
    public void setLastCompletedDate(LocalDate lastCompletedDate) { this.lastCompletedDate = lastCompletedDate; }
}