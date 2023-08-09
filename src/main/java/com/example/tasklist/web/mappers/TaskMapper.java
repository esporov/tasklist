package com.example.tasklist.web.mappers;

import com.example.tasklist.domain.task.Task;
import com.example.tasklist.web.dto.task.TaskDTO;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTO(List<Task> tasks);

    Task toEntity(TaskDTO taskDTO);
}
