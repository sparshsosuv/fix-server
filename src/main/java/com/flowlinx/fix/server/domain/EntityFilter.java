package com.flowlinx.fix.server.domain;

import java.io.Serializable;

public class EntityFilter<E extends Serializable> {

	private final E condition;

	public EntityFilter(E entity) {
		this.condition = entity;
	}

	public E getCondition() {
		return condition;
	}

}
