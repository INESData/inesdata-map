package com.inesdatamap.mapperbackend.exceptions;

import java.io.Serial;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.inesdatamap.mapperbackend.controllers.errors.BaseErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorCode;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorResponse;
import com.inesdatamap.mapperbackend.controllers.errors.ErrorValidation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Exception for validation errors
 * <p>
 * UNPROCESSABLE_ENTITY(422, "Unprocessable Entity")
 *
 * @author gmv
 */
public class DataValidationException extends BaseException {

	@Serial
	private static final long serialVersionUID = 1175640357421703701L;
	private static final String MESSAGE = "Parameter validation error. Error: {0}";
	private static final ErrorCode CODE_ERROR = BaseErrorCode.VALIDATION;
	private final transient List<ErrorValidation> errorsValidation;

	/**
	 * Constructor
	 *
	 * @param message
	 * 		the validation error message
	 * @param errorsValidation
	 * 		the list of validation errors
	 */
	public DataValidationException(String message, List<ErrorValidation> errorsValidation) {
		super(MessageFormat.format(MESSAGE, message));
		this.errorsValidation = new ArrayList<>(errorsValidation);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 * 		the validation error message
	 */
	public DataValidationException(String message) {
		super(MessageFormat.format(MESSAGE, message));
		this.errorsValidation = Collections.emptyList();
	}

	/**
	 * Constructor
	 *
	 * @param ex
	 *        {@link ConstraintViolationException} the exception
	 */
	public DataValidationException(ConstraintViolationException ex) {
		super(MessageFormat.format(MESSAGE, ex.getLocalizedMessage()));
		errorsValidation = formatInvalidParams(ex);
	}

	/**
	 * Constructor
	 *
	 * @param ex
	 *        {@link MethodArgumentNotValidException} the exception
	 */
	public DataValidationException(MethodArgumentNotValidException ex) {
		super(MessageFormat.format(MESSAGE, ex.getLocalizedMessage()));
		errorsValidation = formatInvalidParams(ex);
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorCode()
	 */
	@Override
	public ErrorCode getErrorCode() {
		return CODE_ERROR;
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return "Invalid parameters";
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorDetails()
	 */
	@Override
	public String getErrorDetails() {
		return getMessage();
	}

	/**
	 * Gets validation errors
	 *
	 * @return {@link List}&lt;{@link ErrorValidation}&gt; the list of validation errors
	 */
	public List<ErrorValidation> getErrorsValidation() {
		return new ArrayList<>(errorsValidation);
	}

	/**
	 * (non-javadoc)
	 *
	 * @see BaseException#getErrorResponse()
	 */
	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(getErrorCode(), getErrorMessage(), getErrorsValidation());
	}

	private static List<ErrorValidation> formatInvalidParams(ConstraintViolationException ex) {
		List<ErrorValidation> invalidParams = new ArrayList<>();
		ex.getConstraintViolations().forEach(val -> invalidParams.add(createErrorValidation(val)));
		return invalidParams;
	}

	private static List<ErrorValidation> formatInvalidParams(MethodArgumentNotValidException ex) {
		List<ErrorValidation> invalidParams = new ArrayList<>();
		ex.getBindingResult().getGlobalErrors().forEach(val -> invalidParams.add(createErrorValidation(val)));
		ex.getBindingResult().getFieldErrors().forEach(val -> invalidParams.add(createErrorValidation(val)));
		return invalidParams;
	}

	private static ErrorValidation createErrorValidation(ConstraintViolation<?> violation) {
		String field = (violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : null);
		return new ErrorValidation(field, "", violation.getMessage());
	}

	private static ErrorValidation createErrorValidation(FieldError fieldError) {
		return new ErrorValidation(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
	}

	private static ErrorValidation createErrorValidation(ObjectError objError) {
		return new ErrorValidation(objError.getObjectName().replaceAll("(?i)dto", ""), objError.getCode(), objError.getDefaultMessage());
	}

}

