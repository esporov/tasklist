package com.example.tasklist.web.controller;

import com.example.tasklist.domain.task.Task;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.web.dto.task.TaskDTO;
import com.example.tasklist.web.dto.validation.OnUpdate;
import com.example.tasklist.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PutMapping
    @Operation(summary = "Update task")
    @PreAuthorize("canAccessTask(#taskDTO)")
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDTO(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDTO(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
