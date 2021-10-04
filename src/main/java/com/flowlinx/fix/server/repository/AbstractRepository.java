package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.FixEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractRepository<E extends Serializable>  extends PagingAndSortingRepository<E, Long>,  JpaSpecificationExecutor<E> {


}
