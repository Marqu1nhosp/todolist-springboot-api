package br.com.marcosporto.todolist.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String description;
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String idUser;


    public void setTitle(String title) throws Exception {
        if(title.length() > 50){
            throw new Exception("O campo title deve conter no máximo 50 caracteres.");
        }
        
        this.title = title;
    }
}
