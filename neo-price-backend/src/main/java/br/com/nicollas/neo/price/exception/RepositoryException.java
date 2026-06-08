package br.com.nicollas.neo.price.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RepositoryException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public RepositoryException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

}
