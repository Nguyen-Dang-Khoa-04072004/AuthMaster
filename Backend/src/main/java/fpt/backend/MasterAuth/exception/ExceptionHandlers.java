package fpt.backend.MasterAuth.exception;

import fpt.backend.MasterAuth.response.ExceptionResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({UsernameNotFoundException.class, EmailAlreadyExisted.class, BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception e){
        return ResponseEntity.badRequest().body(
            ExceptionResponse.builder()
                .code(400)
                .message(e.getMessage())
                .build()
        );
    }
}
