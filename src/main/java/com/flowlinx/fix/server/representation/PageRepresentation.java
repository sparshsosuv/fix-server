package com.flowlinx.fix.server.representation;

import com.flowlinx.fix.server.domain.FixEntity;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.tree.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class PageRepresentation<E extends FixEntity> implements Serializable {

	private static final long serialVersionUID = -6793768296376380855L;

	private String sort;
	private boolean desc;
	private int number;
	private int size;
	private long totalElements;
	private int totalPages;
	private List<E> content;

	public PageRepresentation(Page<E> page, List<E> content, boolean desc) {
		this.setNumber(page.getNumber());
		this.setTotalElements( page.getTotalElements() );
		this.setSize(page.getSize());
		this.setTotalPages(page.getTotalPages());
		this.setContent( content );
		this.setSort( page.getSort().toString() );
		this.setDesc( desc );
	}


}
