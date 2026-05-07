package ru.netology.todoservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.todoservice.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void getAllTasks_empty() {
        List<Task> tasks = taskService.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void createTask() {
        Task task = new Task();
        task.setTitle("Test task");

        Task created = taskService.createTask(task);

        assertNotNull(created.getId());
        assertEquals("Test task", created.getTitle());
        assertFalse(created.getCompleted());
    }

    @Test
    void getTaskById_found() {
        Task task = new Task();
        task.setTitle("Find me");
        Task created = taskService.createTask(task);

        Task found = taskService.getTaskById(created.getId());

        assertNotNull(found);
        assertEquals("Find me", found.getTitle());
    }

    @Test
    void getTaskById_notFound() {
        Task found = taskService.getTaskById(999L);
        assertNull(found);
    }

    @Test
    void updateTask() {
        Task task = new Task();
        task.setTitle("Original");
        Task created = taskService.createTask(task);

        Task updated = new Task();
        updated.setTitle("Updated");
        updated.setCompleted(true);

        Task result = taskService.updateTask(created.getId(), updated);

        assertNotNull(result);
        assertEquals("Updated", result.getTitle());
        assertTrue(result.getCompleted());
    }

    @Test
    void updateTask_notFound() {
        Task updated = new Task();
        updated.setTitle("Nope");

        Task result = taskService.updateTask(999L, updated);

        assertNull(result);
    }

    @Test
    void deleteTask() {
        Task task = new Task();
        task.setTitle("Delete me");
        Task created = taskService.createTask(task);

        boolean deleted = taskService.deleteTask(created.getId());

        assertTrue(deleted);
        assertNull(taskService.getTaskById(created.getId()));
    }

    @Test
    void deleteTask_notFound() {
        boolean deleted = taskService.deleteTask(999L);
        assertFalse(deleted);
    }
}