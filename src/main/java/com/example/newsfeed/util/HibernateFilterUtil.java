package com.example.newsfeed.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class HibernateFilterUtil {

    @PersistenceContext
    private final EntityManager entityManager;

    public void enableActiveUserFilter() {
        entityManager.unwrap(Session.class).enableFilter("activeUserFilter");
    }

    public void disableActiveUserFilter() {
        entityManager.unwrap(Session.class).disableFilter("activeUserFilter");
    }

}