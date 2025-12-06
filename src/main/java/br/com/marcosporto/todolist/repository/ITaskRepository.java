package br.com.marcosporto.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.marcosporto.todolist.model.TaskModel;

import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, String> {
    List<TaskModel> findByIdUser(String idUser);
}
