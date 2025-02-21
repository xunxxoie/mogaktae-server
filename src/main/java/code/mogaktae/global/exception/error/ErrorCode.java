package code.mogaktae.global.exception.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    Integer getCode();
    String getMessage();
}
