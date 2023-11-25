package ksp7331.practice.libraryAPI.advice;

import ksp7331.practice.libraryAPI.exception.BusinessLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<Void> handleBusinessLogicException(BusinessLogicException e) {
        log.info("error message : {}", e.getMessage());
        return ResponseEntity.status(e.getExceptionCode().getStatus()).build();
    }
    @ExceptionHandler
    public ResponseEntity<Void> handleException(Exception e) {
        log.error("# handle Exception", e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).build();
    }
}
