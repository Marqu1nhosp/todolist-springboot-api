package br.com.marcosporto.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.marcosporto.todolist.model.UserModel;


public interface IUserRepository extends JpaRepository<UserModel, String> {
    
    UserModel findByUsername(String username);
}
