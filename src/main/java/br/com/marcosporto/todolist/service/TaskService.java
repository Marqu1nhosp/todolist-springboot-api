package br.com.marcosporto.todolist.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.marcosporto.todolist.model.TaskModel;
import br.com.marcosporto.todolist.repository.ITaskRepository;

@Service
public class TaskService {

    @Autowired
    private final ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel create(TaskModel taskModel, String idUser) {

        taskModel.setIdUser(idUser);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) ||
                currentDate.isAfter(taskModel.getEndAt())) {

            throw new IllegalArgumentException(
                    "A data de início / término deve ser maior ou igual à data atual.");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            throw new IllegalArgumentException(
                    "A data de início deve ser menor que a data de término.");
        }

        return taskRepository.save(taskModel);
    }

}
