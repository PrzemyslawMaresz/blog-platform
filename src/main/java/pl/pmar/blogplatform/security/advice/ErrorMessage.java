package pl.pmar.blogplatform.security.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    private int StatusCode;
    private Date timestamp;
    private String message;
    private String description;
}
