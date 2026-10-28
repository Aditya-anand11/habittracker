package com.tracker.service;

import com.tracker.model.TaskHabitItem;
import com.tracker.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    // Uses ArrayList and Custom Comparator Sorting
    public ArrayList<TaskHabitItem> getSortedItems(String sortBy) {
        ArrayList<TaskHabitItem> items = repository.findAll();

        Comparator<TaskHabitItem> comparator;
        switch (sortBy != null ? sortBy.toLowerCase() : "priority") {
            case "deadline":
                comparator = Comparator.comparing(TaskHabitItem::getDeadline);
                break;
            case "streak":
                comparator = Comparator.comparingInt(TaskHabitItem::getStreak).reversed();
                break;
            case "priority":
            default:
                comparator = Comparator.comparingInt(TaskHabitItem::getPriorityWeight)
                        .thenComparing(TaskHabitItem::getDeadline);
                break;
        }

        items.sort(comparator);
        return items;
    }

    // Uses HashMap to compute Completion & Retention Statistics
    public Map<String, Object> getStatistics() {
        ArrayList<TaskHabitItem> items = repository.findAll();
        HashMap<String, Object> stats = new HashMap<>();

        int total = items.size();
        int completedCount = 0;
        int overdueCount = 0;
        int activeHabits = 0;
        int maxStreak = 0;

        HashMap<String, Integer> priorityBreakdown = new HashMap<>();
        priorityBreakdown.put("HIGH", 0);
        priorityBreakdown.put("MEDIUM", 0);
        priorityBreakdown.put("LOW", 0);

        for (TaskHabitItem item : items) {
            if (item.isCompleted()) completedCount++;
            if (item.isOverdue()) overdueCount++;
            if ("HABIT".equalsIgnoreCase(item.getCategory())) {
                activeHabits++;
                if (item.getStreak() > maxStreak) maxStreak = item.getStreak();
            }
            priorityBreakdown.put(item.getPriority(),
                    priorityBreakdown.getOrDefault(item.getPriority(), 0) + 1);
        }

        double completionRate = total == 0 ? 0 : Math.round(((double) completedCount / total) * 1000.0) / 10.0;

        stats.put("totalItems", total);
        stats.put("completedItems", completedCount);
        stats.put("overdueAlerts", overdueCount);
        stats.put("activeHabits", activeHabits);
        stats.put("maxStreak", maxStreak);
        stats.put("completionRate", completionRate);
        stats.put("priorityDistribution", priorityBreakdown);

        return stats;
    }

    public void createItem(TaskHabitItem item) {
        if (item.getDeadline() == null) {
            item.setDeadline(LocalDate.now());
        }
        repository.save(item);
    }

    // Handles Recurring Habit Resets & Streak Retention using Date-Time API
    public void completeOrAdvanceItem(Long id) {
        TaskHabitItem item = repository.findById(id);
        LocalDate today = LocalDate.now();

        if ("HABIT".equalsIgnoreCase(item.getCategory())) {
            item.setStreak(item.getStreak() + 1);
            item.setLastCompletedDate(today);

            // Advance recurring deadline automatically
            if ("DAILY".equalsIgnoreCase(item.getFrequency())) {
                item.setDeadline(today.plusDays(1));
            } else if ("WEEKLY".equalsIgnoreCase(item.getFrequency())) {
                item.setDeadline(today.plusWeeks(1));
            }
            item.setCompleted(false); // Remains active for next cycle
        } else {
            // Standard one-time task toggle
            item.setCompleted(!item.isCompleted());
            if (item.isCompleted()) {
                item.setLastCompletedDate(today);
            }
        }
        repository.update(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }
}