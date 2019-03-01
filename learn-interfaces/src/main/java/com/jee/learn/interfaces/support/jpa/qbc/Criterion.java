package com.jee.learn.interfaces.support.jpa.qbc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 条件接口 用户提供条件表达式接口
 * 
 * @author https://github.com/344399160/JPAManager
 */
public interface Criterion {
    public enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR, BETWEEN, ISNULL, ISNOTNULL, ISEMPTY, ISNOTEMPTY
    }

    public enum MatchMode {
        START, END, ANYWHERE
    }

    public enum Projection {
        MAX, MIN, AVG, LENGTH, SUM, COUNT
    }

    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}
