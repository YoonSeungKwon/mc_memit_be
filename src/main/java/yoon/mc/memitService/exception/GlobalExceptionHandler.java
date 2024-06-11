package yoon.mc.memitService.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MemitException.class})
    public ResponseEntity<String> exceptionHandler(MemitException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

}
