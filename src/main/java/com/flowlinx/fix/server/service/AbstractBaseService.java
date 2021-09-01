package com.flowlinx.fix.server.service;

import com.flowlinx.fix.server.domain.FixEntity;
import com.flowlinx.fix.server.domain.EntityFilter;
import com.flowlinx.fix.server.repository.AbstractRepository;
import com.flowlinx.fix.server.utils.AppUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBaseService<T extends FixEntity> {

	protected static final String PATH_SEPARATOR = ".";

	private final AbstractRepository<T> repository;

    protected Class<T> entityClass;

	@PersistenceContext
	public EntityManager entityManager;

    public AbstractBaseService(AbstractRepository<T> repository) {
    		this.repository = repository;
    }

    @PostConstruct
    public void init() {
    		entityClass = AppUtils.getParameterizedClassByIndex( getClass(),  0 );
    }


    @Transactional(readOnly = true)
    public Optional<T> get(Long id) {
        return repository.findById( id );
    }

	/**
	 * Returns all entities sorted by the given options.
	 *
	 * @param sort
	 * @return all entities sorted by the given options
	 */
	public Iterable<T> findAll(Sort sort){
		return repository.findAll( sort );
	};

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable
	 * @return a page of entities
	 */
	public Page<T> findAll(Pageable pageable){
		return repository.findAll(pageable);
	}

	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity will never be {@literal null}.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T save(T entity) {
		return repository.save( entity );
	}

	/**
	 * Saves all given entities.
	 *
	 * @param entities must not be {@literal null}.
	 * @return the saved entities will never be {@literal null}.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	public Iterable<T> saveAll(Iterable<T> entities){
		return repository.saveAll( entities );
	}

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	public Optional<T> findById(Long id){
		return repository.findById(id);
	}

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	public boolean existsById(Long id) {
		return repository.existsById(id);
	}

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	public Iterable<T> findAll(final Integer offset, final Integer limit, String sort, boolean asc) {
		return repository.findAll( PageRequest.of( offset, limit, Sort.by( asc ? Direction.ASC : Direction.DESC, sort) ) );
	}

	/**
	 * Returns all instances of the type with the given IDs.
	 *
	 * @param ids
	 * @return
	 */
	public Iterable<T> findAllById(Iterable<Long> ids){
		return repository.findAllById(ids);
	}

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities
	 */
	public long count() {
		return repository.count();
	}

	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	@Transactional
	public void delete(T entity) {
		repository.delete( entity );
	}

	/**
	 * Deletes the given entities.
	 *
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	@Transactional
	public  void deleteAll(Iterable<? extends T> entities) {
		repository.deleteAll( entities );
	}

	/**
	 * Returns a single entity matching the given {@link Specification} or {@link Optional#empty()} if none found.
	 *
	 * @param spec can be {@literal null}.
	 * @return never {@literal null}.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if more than one entity found.
	 */
	public Optional<T> findOne(@Nullable Specification<T> spec){
		return repository.findOne(spec);
	}

	/**
	 * Returns all entities matching the given {@link Specification}.
	 *
	 * @param spec can be {@literal null}.
	 * @return never {@literal null}.
	 */
	public List<T> findAll(@Nullable Specification<T> spec){
		return repository.findAll(spec);
	}

	/**
	 * Returns a {@link Page} of entities matching the given {@link Specification}.
	 *
	 * @param spec can be {@literal null}.
	 * @param pageable must not be {@literal null}.
	 * @return never {@literal null}.
	 */
	public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable){
		return repository.findAll(spec, pageable);
	}

	/**
	 * Returns all entities matching the given {@link Specification} and {@link Sort}.
	 *
	 * @param spec can be {@literal null}.
	 * @param sort must not be {@literal null}.
	 * @return never {@literal null}.
	 */
	public List<T> findAll(@Nullable Specification<T> spec, Sort sort){
		return repository.findAll(spec, sort);
	}

	/**
	 * Returns the number of instances that the given {@link Specification} will return.
	 *
	 * @param spec the {@link Specification} to count instances for. Can be {@literal null}.
	 * @return the number of instances.
	 */
	public long count(@Nullable Specification<T> spec) {
		return repository.count();
	}

	public Page<T> findAll(Pageable pageable, EntityFilter<T> filter) {
		final Specification<T> spec = specificationByFilter(filter);
		return repository.findAll( spec, pageable );
	}

	public long count(EntityFilter<T> filter) {
		return repository.count( specificationByFilter( filter ) );
	}


	/**
	 * Aims to build a specification based on the filter.
	 *
	 * @param filter
	 * @return
	 */
	public Specification<T> specificationByFilter(EntityFilter<T> filter) {

		return new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isNotNull( root.get("time") );
			}
		};
	}


    protected final Predicate notNullId(Root<T> root, CriteriaBuilder cb) {
		return cb.and( root.get( "time").isNotNull() );
    }

    protected final Specification<T> ilike(String attrPath, String val) {
		return (Specification<T>) (root, query, cb) -> cb.or ( StringUtils.isNotBlank(val)
				? cb.like( cb.lower( evaluateAttrPath( attrPath, root) ), "%" + val.toLowerCase() + "%" )
				: cb.isNotNull( root ) );
    }

    protected final Specification<T> memberOf(String attrPath, Collection<?> values) {
    	return (Specification<T>) (root, query, cb) -> cb.and(
				CollectionUtils.isEmpty( values ) ? notNullId(root, cb) : root.get( attrPath ).in( values ) );
    }

    protected final Specification<T> eq(String attrPath, Object val) {

    	return new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and( val != null
						? cb.and( cb.equal( evaluateAttrPath( attrPath, root), val ) )
						: cb.isNotNull( root ) );

			}
		};
    }

	protected final Specification<T> notEqual(String attrPath, Object val) {

		return new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and( val != null
						? cb.and( cb.notEqual( evaluateAttrPath( attrPath, root), val ) )
						: cb.isNotNull( root ) );

			}
		};
	}

	protected final Specification<T> isNotMember(String attrPath, Collection values) {

		return new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and( CollectionUtils.isNotEmpty( values )
						? cb.and( cb.isNotMember( values, evaluateAttrPath( attrPath, root) ) )
						: cb.isNotNull( root ) );

			}
		};
	}

    protected final Specification<T> notNull() {

    	return new Specification<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and( cb.isNotNull( evaluateAttrPath( "time", root) ) );
			}
		};
    }


    protected final <N> Path<N> evaluateAttrPath(final String attrPath, final Root<T> root) {
        if (attrPath == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        if (attrPath.contains(PATH_SEPARATOR)) {
            Path<N> path = null;
            for (String p : StringUtils.split(attrPath, PATH_SEPARATOR))
                if (path == null) path = root.get(p);
                else path = path.get(p);
            return path;
        }
        return root.get(attrPath);
    }

	public AbstractRepository<T> getRepository() {
		return repository;
	}

}
