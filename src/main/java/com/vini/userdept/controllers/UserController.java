package com.vini.userdept.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vini.userdept.model.UserModel;
import com.vini.userdept.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserModel>> findAll() {
        List<UserModel> users = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findByInd(@PathVariable(value = "id") Long id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");

        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @PostMapping("/users")
    public ResponseEntity<?> insert(@RequestBody UserModel userModel) {
        var email = this.userRepository.findByEmail(userModel.getEmail());

        if (email != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email do usuario já existe");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Long id, @RequestBody UserModel userModel) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não existe");
        }

        UserModel existingUser = userRepository.findById(id).get();
        existingUser.setName(userModel.getName());
        existingUser.setEmail(userModel.getEmail());
        existingUser.setDepartmentModel(userModel.getDepartmentModel());

        userRepository.save(existingUser);

        return ResponseEntity.status(HttpStatus.OK).body(existingUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não existe");
        }

        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
