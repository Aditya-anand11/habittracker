CREATE TABLE IF NOT EXISTS task_habit_items (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(20) NOT NULL,       -- 'TASK' or 'HABIT'
    priority VARCHAR(20) NOT NULL,       -- 'HIGH', 'MEDIUM', 'LOW'
    frequency VARCHAR(20) NOT NULL,      -- 'ONCE', 'DAILY', 'WEEKLY'
    deadline DATE NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    streak INT DEFAULT 0,
    last_completed_date DATE
);