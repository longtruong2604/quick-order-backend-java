package com.backend.backend_java.repository;

import static com.backend.backend_java.util.AppConstant.SEARCH_OPERATOR;
import static com.backend.backend_java.util.AppConstant.SORT_BY;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.backend.backend_java.dto.response.PageResponse;
import com.backend.backend_java.model.Address;
import com.backend.backend_java.model.User;
import com.backend.backend_java.repository.criteria.SearchCriteria;
import com.backend.backend_java.repository.criteria.UserSearchCriteriaConsumer;

import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class SearchRepository {

    @PersistenceContext // Manage the lifecycle of the entity manager
    private EntityManager entityManager;

    private static final String LIKE_FORMAT = "%%%s%%";

    public PageResponse<?> searchUser(int pageNo, int pageSize, String search, String sortBy) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT new com.backend.backend_java.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.phone, u.email, u.username) FROM User u WHERE 1=1");

        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" AND lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" OR lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" OR lower(u.email) like lower(:email)");
            sqlQuery.append(" OR lower(u.username) like lower(:username)");
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                sqlQuery.append(String.format(" ORDER BY u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("firstName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("lastName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("email", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("username", String.format(LIKE_FORMAT, search));
        }

        selectQuery.setFirstResult(pageNo * pageSize);
        selectQuery.setMaxResults(pageSize);
        List<?> users = selectQuery.getResultList();

        StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) FROM User u");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" WHERE lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" OR lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" OR lower(u.email) like lower(?3)");
            sqlCountQuery.append(" OR lower(u.username) like lower(?4)");
        }

        Query countQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            countQuery.setParameter(1, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(2, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(3, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(4, String.format(LIKE_FORMAT, search));
            countQuery.getSingleResult();
        }

        Long totalElements = (Long) countQuery.getSingleResult();

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<?> page = new PageImpl<>(users, pageable, totalElements);

        return PageResponse.builder()
                .data(users)
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .currentPage(pageNo)
                .pageSize(pageSize)
                .build();
    }

    public PageResponse<?> advanceSearchUser(int pageNo, int pageSize, String sortBy, String address,
            String... search) {

        // TODO: Get users

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        if (search != null) {
            for (String searchBy : search) {
                Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
                Matcher matcher = pattern.matcher(searchBy);
                if (matcher.find()) {
                    searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        // TODO: Get total elements

        List<User> users = getUsers(pageNo, pageSize, sortBy, address, searchCriteriaList);

        return users.isEmpty() ? PageResponse.builder().build()
                : PageResponse.builder()
                        .data(users)
                        .totalPage(1)
                        .totalElement((long) users.size())
                        .currentPage(pageNo)
                        .pageSize(pageSize)
                        .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, String sortBy, String address,
            List<SearchCriteria> searchCriteriaList) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // TODO: Handle dynamic query
        Predicate predicate = criteriaBuilder.conjunction(); // predicate: condition, conjunction creates an AND
                                                             // condition
        UserSearchCriteriaConsumer searchCriteriaConsumer = new UserSearchCriteriaConsumer(criteriaBuilder,
                predicate,
                root);

        if (StringUtils.hasLength(address)) {
            Join<User, Address> join = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(join.get("city"), "%" + address + "%");
            searchCriteriaList.add(new SearchCriteria("address", ":", address));
            query.where(predicate, addressPredicate);

        } else {
            searchCriteriaList.forEach(searchCriteriaConsumer);
            predicate = searchCriteriaConsumer.getPredicate();
            query.where(predicate);
        }

        if (StringUtils.hasLength(sortBy)) {
            System.out.println(sortBy);
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);

            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }
        return entityManager.createQuery(query)
                .setFirstResult(pageNo * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
