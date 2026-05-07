package ru.netology.todoservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.todoservice.model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class TaskService {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Task> getAllTasks() {
        log.info("Getting all tasks, count: {}", tasks.size());
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(Long id) {
        log.info("Getting task by id: {}", id);
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        Long id = idGenerator.getAndIncrement();
        task.setId(id);
        tasks.put(id, task);
        log.info("Created task with id: {}", id);
        return task;
    }

    public Task updateTask(Long id, Task updatedTask) {
        log.info("Updating task with id: {}", id);
        Task existingTask = tasks.get(id);
        if (existingTask == null) {
            return null;
        }
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setCompleted(updatedTask.getCompleted());
        return existingTask;
    }

    public boolean deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        return tasks.remove(id) != null;
    }
}