package com.flowlinx.fix.server.type;

import java.util.Arrays;
import java.util.Optional;

public enum Authority {

	ROLE_USER, ROLE_ADMIN, ROLE_TRADER, ROLE_SUPERVISOR, ROLE_EXTERNAL_USER, ROLE_WORKFLOW_CHAT;

	public static Authority from(String role){
		final Optional<Authority> opt = Arrays.stream( Authority.values() )
				.filter(a -> a.name().equalsIgnoreCase( role ) ).findFirst();
		return opt.isPresent() ? opt.get() : null;
	}
}
