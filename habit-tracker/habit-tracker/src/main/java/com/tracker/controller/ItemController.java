package com.tracker.controller;

import com.tracker.model.TaskHabitItem;
import com.tracker.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<TaskHabitItem> getAllItems(@RequestParam(defaultValue = "priority") String sortBy) {
        return service.getSortedItems(sortBy);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return service.getStatistics();
    }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody TaskHabitItem item) {
        service.createItem(item);
        return ResponseEntity.ok("Item saved successfully");
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<String> toggleItem(@PathVariable Long id) {
        service.completeOrAdvanceItem(id);
        return ResponseEntity.ok("Item updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.ok("Item deleted");
    }
}