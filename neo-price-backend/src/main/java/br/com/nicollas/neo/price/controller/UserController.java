package br.com.nicollas.neo.price.controller;

import br.com.nicollas.neo.price.domain.dto.user.UserDTO;
import br.com.nicollas.neo.price.domain.model.User;
import br.com.nicollas.neo.price.security.Token;
import br.com.nicollas.neo.price.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*") // Significa que irá ser liberado todas requisições que vierem da máquina, se não, fica restrito e só dá pra fazer pelo Postman.
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<@NonNull List<User>> findAll(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull User> findById(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<@NonNull User> create(@Valid @RequestBody User user){
        User savedUser = userService.save(user);

        // Uniform Resource Identifier ou Identificador Uniforme de Recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Pega o http://localhost:8080/users
                .path("/{id}") // Adiciona /{id} na url
                .buildAndExpand(savedUser.getUserId()) // Substitui /{id} pelo ID real do usuário
                .toUri(); // Converte para URI (o endereço único de um recurso na API)

        return ResponseEntity.created(location).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull User> update(@Valid @RequestBody User user, @PathVariable Long id){
        User updatedUser = userService.update(user, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull Token> login(@Valid @RequestBody UserDTO userDTO){ // @Valid é para validar o @NotBlank na requisição.
        Token token = userService.generateToken(userDTO);
        if(token != null){
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).build();
    }

    // Validação de campos.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

}
