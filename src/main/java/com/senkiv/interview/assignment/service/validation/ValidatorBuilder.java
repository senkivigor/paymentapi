package com.senkiv.interview.assignment.service.validation;

import java.util.Optional;

import com.senkiv.interview.assignment.domain.entity.Transaction;

public class ValidatorBuilder {
	private ValidationRuleImpl first;

	private ValidatorBuilder() {
	}

	public static ValidatorBuilder validatorBuilder() {
		return new ValidatorBuilder();
	}

	public SuccessorBuilder first(ValidationRule validationRule) {
		first = new ValidationRuleImpl(validationRule);
		return new SuccessorBuilder(first);
	}

	public class SuccessorBuilder {
		private ValidationRuleImpl current;

		private SuccessorBuilder(ValidationRuleImpl current) {
			this.current = current;
		}

		public SuccessorBuilder successor(ValidationRule successor) {
			ValidationRuleImpl successorWrapper = new ValidationRuleImpl(successor);
			current.setSuccessor(successorWrapper);
			current = successorWrapper;
			return this;
		}

		public Validator build() {
			return new ValidatorImpl(first);
		}
	}

	private static class ValidationRuleImpl implements ValidationRule {
		private final ValidationRule delegate;
		private ValidationRule successor;

		public ValidationRuleImpl(ValidationRule delegate) {
			this.delegate = delegate;
		}

		private void setSuccessor(ValidationRuleImpl successor) {
			this.successor = successor;
		}

		@Override
		public Optional<ErrorCode> validate(Transaction transaction) {
			Optional<ErrorCode> result = delegate.validate(transaction);
			if (delegate.validate(transaction).isPresent()) {
				return result;
			} else if (successor != null) {
				return successor.validate(transaction);
			}
			return Optional.empty();
		}
	}

	private static class ValidatorImpl implements Validator {
		private final ValidationRule first;

		public ValidatorImpl(ValidationRule first) {
			this.first = first;
		}

		@Override
		public Optional<ErrorCode> validate(Transaction transaction) {
			return first.validate(transaction);
		}
	}
}
