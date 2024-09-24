package com.backend.backend_java.repository.criteria;

import java.util.function.Consumer;

import com.backend.backend_java.model.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSearchCriteriaConsumer implements Consumer<SearchCriteria> {

    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root<User> root;

    @Override
    public void accept(SearchCriteria criteria) {
        if (criteria.getOperation().equals(">")) {
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
        } else if (criteria.getOperation().equals("<")) {
            predicate = builder.and(predicate,
                    builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
        } else {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate,
                        builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%"));
            } else { // For non string types like Integer, Long, etc, we dont want to use like
                predicate = builder.and(predicate, builder.equal(root.get(criteria.getKey()), criteria.getValue()));
            }
        }
    }
}
