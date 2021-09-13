package com.flowlinx.fix.server.repository;

import com.flowlinx.fix.server.domain.FixEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AbstractRepository<E extends FixEntity>  extends PagingAndSortingRepository<E, Long>,  JpaSpecificationExecutor<E> {


}
