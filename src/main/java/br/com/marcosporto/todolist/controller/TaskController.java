package br.com.marcosporto.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.marcosporto.todolist.model.TaskModel;
import br.com.marcosporto.todolist.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var task = taskService.create(taskModel, idUser.toString());

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

   /*  @GetMapping("/")
    public List<TaskModel> getTask(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasksIdUser = this.taskRepository.findByIdUser(idUser.toString());

        return tasksIdUser;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, @PathVariable String id,
            HttpServletRequest request) {
        var task = this.taskRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada!");
        }
        
        var idUser = request.getAttribute("idUser");
        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário não tem permissão para alterar essa tarefa.");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }*/
}
