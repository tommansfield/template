package com.tom.template.exception;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.tom.template.dto.ErrorResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());
		List<String> errors = ex.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}
	
	@ExceptionHandler({ AuthRequestException.class, OAuth2Exception.class, AccessDeniedException.class })
	public ResponseEntity<ErrorResponse> customHandleAuthRequest(Exception ex, HttpServletRequest request) {
		String path = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		ErrorResponse errors = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), path);
		return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> customHandleBadRequest(Exception ex, HttpServletRequest request) {
		String path = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		ErrorResponse errors = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), path);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ErrorResponse> customHandleEmail(Exception ex, HttpServletRequest request) {
		String path = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		ErrorResponse errors = new ErrorResponse(HttpStatus.BAD_GATEWAY.value(), ex.getMessage(), path);
		return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> customHandleNotFound(Exception ex, HttpServletRequest request) {
		String path = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        ErrorResponse errors = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), path);
		return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
}
