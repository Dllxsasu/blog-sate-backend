package com.jeremias.dev.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotBlankOrNullValidator implements ConstraintValidator<NotBlankOrNull, String> {

	private final transient NotBlankValidator notBlankValidator = new NotBlankValidator();

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if (value == null) {
			return true;
		}
		return notBlankValidator.isValid(value, context);
	}

}
