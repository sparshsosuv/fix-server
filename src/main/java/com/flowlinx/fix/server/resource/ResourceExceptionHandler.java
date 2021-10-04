package com.flowlinx.fix.server.resource;

import com.flowlinx.fix.server.exception.PreconditionalFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(ResourceExceptionHandler.class);

	@ExceptionHandler(PreconditionalFailedException.class)
	public ResponseEntity<Object> handleValidation(PreconditionalFailedException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

		String bodyOfResponse = "This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> constraintViolation(DataIntegrityViolationException ex, WebRequest request) {

		final String body = "{" + ex.getMostSpecificCause().getMessage() + "}";

		return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> defaultErrorHandler(WebRequest request, Exception e) throws Exception {

		// If the exception is annotated with @ResponseStatus rethrow it
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		e.printStackTrace();

		LOGGER.error(e.getMessage(), e);

		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> defaultErrorHandler(WebRequest request, BadCredentialsException e) throws Exception {
		return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}


	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ex.printStackTrace();
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}


}
