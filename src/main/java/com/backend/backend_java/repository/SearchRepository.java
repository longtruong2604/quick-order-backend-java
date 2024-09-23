package com.backend.backend_java.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.backend.backend_java.dto.response.PageResponse;
import com.backend.backend_java.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class SearchRepository {

    @PersistenceContext // Manage the lifecycle of the entity manager
    private EntityManager entityManager;

    public PageResponse<?> searchUser(int pageNo, int pageSize, String search, String sortBy, String order) {
        // TODO: Query the database to get all users with the given search string
        //

        StringBuilder query = new StringBuilder("SELECT u FROM User u");
        Query q = entityManager.createQuery(query.toString(), User.class);
        List<?> users = q.getResultList();
        System.out.println(users.size());
        return null;
    }

}
