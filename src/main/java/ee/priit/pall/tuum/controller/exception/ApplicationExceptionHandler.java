package ee.priit.pall.tuum.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<RestErrorMessage> handleApplicationException(ApplicationException e) {
        RestErrorMessage errorMessage = new RestErrorMessage(e.getErrorCode(), e.getMessage());
        logException(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
        String error = e.getBindingResult().getFieldErrors()
          .stream()
          .findFirst()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .orElse("");
        RestErrorMessage errorMessage = new RestErrorMessage(ErrorCode.VALIDATION_ERROR, error);
        logException(ErrorCode.VALIDATION_ERROR, error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<RestErrorMessage> handleAllOtherExceptions(Exception e) {
        RestErrorMessage errorMessage = new RestErrorMessage(ErrorCode.SERVER_RUNTIME_ERROR,
          e.getMessage());
        logException(ErrorCode.VALIDATION_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    private void logException(ErrorCode errorCode, String message) {
        log.error("{} \t|\t {}", errorCode, message);
    }
}
