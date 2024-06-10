package yoon.mc.memitService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemitException extends RuntimeException{

    private final String message;

    private final HttpStatus status;

    public MemitException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
