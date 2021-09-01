package com.flowlinx.fix.server.domain;

public class EntityFilter<E extends FixEntity> {

	private final E condition;

	public EntityFilter(E entity) {
		this.condition = entity;
	}

	public E getCondition() {
		return condition;
	}

}
