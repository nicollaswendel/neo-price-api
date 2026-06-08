package br.com.nicollas.neo.price.service;

import br.com.nicollas.neo.price.domain.dto.user.UserDTO;
import br.com.nicollas.neo.price.domain.model.User;
import br.com.nicollas.neo.price.exception.RepositoryException;
import br.com.nicollas.neo.price.repository.UserRepository;
import br.com.nicollas.neo.price.security.Token;
import br.com.nicollas.neo.price.security.TokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RepositoryException("The user doesn't exists in database.", HttpStatus.NOT_FOUND));
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RepositoryException("The user doesn't exists in database.", HttpStatus.NOT_FOUND));

        userRepository.deleteById(user.getUserId());
    }

    public User save(User user) {
        User savedUser = new User();

        String encoder = passwordEncoder.encode(user.getPassword());

        savedUser.setName(user.getName());
        savedUser.setPassword(encoder);
        savedUser.setEmail(user.getEmail());

        userRepository.save(savedUser);
        return savedUser;
    }

    public User update(User user, Long id) {

        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new RepositoryException("The user doesn't exists in database.", HttpStatus.NOT_FOUND));

        updatedUser.setName(user.getName());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());

        userRepository.save(updatedUser);
        return updatedUser;
    }

    public Token generateToken(@Valid UserDTO userDTO) {

        User user = userRepository
                .findByEmail(userDTO.getEmail())
                .orElse(null);

        if (user != null) {
            boolean valid = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());

            if (valid){
                return new Token(TokenUtil.createToken(user));
            }
        }
        return null;
    }

}
