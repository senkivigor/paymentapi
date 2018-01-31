package com.senkiv.interview.assignment.domain.mapper;

@FunctionalInterface
public interface EntityMapper<E, D> {
    E mapToEntity(D dto);
}
