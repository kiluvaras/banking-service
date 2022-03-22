package ee.priit.pall.tuum.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorMessage {
    private ErrorCode errorCode;
    private String message;
}
