package com.senkiv.interview.assignment.service.validation;

import java.util.Optional;

import com.senkiv.interview.assignment.domain.entity.Transaction;

@FunctionalInterface
public interface ValidationRule {
	Optional<ErrorCode> validate(final Transaction transaction);
}
