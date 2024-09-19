package com.backend.backend_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}

// JpaRepository is a JPA specific extension of Repository. It contains the full
// API of CrudRepository and PagingAndSortingRepository.
// Long is the type of the entity key

// Example methods provided by JpaRepository:
// User save(User entity)
// Optional<User> findById(Long id)
// List<User> findAll()
// void deleteById(Long id)

// JpaSpecificationExecutor provides support for building dynamic queries using
// the Specification pattern.

// Example methods provided by JpaSpecificationExecutor:
// List<User> findAll(Specification<User> spec)
// Optional<User> findOne(Specification<User> spec)
// long count(Specification<User> spec)