package com.tracker.repository;

import com.tracker.model.TaskHabitItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TaskHabitItem> rowMapper = (rs, rowNum) -> {
        TaskHabitItem item = new TaskHabitItem();
        item.setId(rs.getLong("id"));
        item.setTitle(rs.getString("title"));
        item.setCategory(rs.getString("category"));
        item.setPriority(rs.getString("priority"));
        item.setFrequency(rs.getString("frequency"));
        
        Date deadlineSql = rs.getDate("deadline");
        if (deadlineSql != null) item.setDeadline(deadlineSql.toLocalDate());
        
        item.setCompleted(rs.getBoolean("completed"));
        item.setStreak(rs.getInt("streak"));
        
        Date lastCompSql = rs.getDate("last_completed_date");
        if (lastCompSql != null) item.setLastCompletedDate(lastCompSql.toLocalDate());
        
        return item;
    };

    public ArrayList<TaskHabitItem> findAll() {
        String sql = "SELECT * FROM task_habit_items";
        List<TaskHabitItem> list = jdbcTemplate.query(sql, rowMapper);
        return new ArrayList<>(list);
    }

    public TaskHabitItem findById(Long id) {
        String sql = "SELECT * FROM task_habit_items WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public int save(TaskHabitItem item) {
        String sql = "INSERT INTO task_habit_items (title, category, priority, frequency, deadline, completed, streak, last_completed_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                item.getTitle(),
                item.getCategory(),
                item.getPriority(),
                item.getFrequency(),
                Date.valueOf(item.getDeadline()),
                item.isCompleted(),
                item.getStreak(),
                item.getLastCompletedDate() != null ? Date.valueOf(item.getLastCompletedDate()) : null
        );
    }

    public int update(TaskHabitItem item) {
        String sql = "UPDATE task_habit_items SET completed = ?, streak = ?, deadline = ?, last_completed_date = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                item.isCompleted(),
                item.getStreak(),
                Date.valueOf(item.getDeadline()),
                item.getLastCompletedDate() != null ? Date.valueOf(item.getLastCompletedDate()) : null,
                item.getId()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM task_habit_items WHERE id = ?", id);
    }
}