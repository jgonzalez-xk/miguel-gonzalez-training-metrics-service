package com.xumak.training.metrics.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "{\"state\": false}")
public class StateFalseException extends RuntimeException {
}
