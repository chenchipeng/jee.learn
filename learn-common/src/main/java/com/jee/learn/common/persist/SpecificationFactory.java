package com.jee.learn.common.persist;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.persist.Condition.Paramerter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpecificationFactory<T> {

    private static final String LIKE_SUFFIX = "%";

    public Specification<T> createSpecifications(Condition con) {
        if (con == null || con.isEmpty()) {
            return null;
        }

        Iterator<Condition.Paramerter> iterator = con.iterator();
        if (con.size() == 1) {
            return Specifications.where(createSpecification(iterator.next()));
        }

        Specifications<T> specifications = Specifications.where(createSpecification(iterator.next()));
        while (iterator.hasNext()) {
            specifications = specifications.and(createSpecification(iterator.next()));
        }

        return specifications;
    }

    private Specification<T> createSpecification(Paramerter p) {
        return createSpecification(p.getFieldName(), p.getOperator(), p.getValue());
    }

    private Specification<T> createSpecification(String property, Operator opt, Object value) {
        if (StringUtils.isBlank(property)) {
            throw new IllegalArgumentException("path can not be null");
        }
        if ((opt != Operator.ISNULL && opt != Operator.ISNOTNULL && opt != Operator.ISEMPTY
                && opt != Operator.ISNOTEMPTY) && value == null) {
            throw new IllegalArgumentException("value can not be null");
        }

        return new Specification<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<?> path = getPath(root, property);
                switch (opt) {
                case EQ:
                    return cb.equal(path, value);
                case NE:
                    return cb.notEqual(path, value);
                case LIKE:
                    if (String.valueOf(value).contains(LIKE_SUFFIX)) {
                        return cb.like((Expression<String>) path, String.valueOf(value));
                    } else {
                        return cb.like((Expression<String>) path, LIKE_SUFFIX + value + LIKE_SUFFIX);
                    }
                case GE:
                    return parseGE(cb, value, path);
                case GT:
                    return parseGT(cb, value, path);
                case LE:
                    return parseLE(cb, value, path);
                case LT:
                    return parseLT(cb, value, path);
                case ISNULL:
                    return cb.isNull(path);
                case ISNOTNULL:
                    return cb.isNotNull(path);
                case ISEMPTY:
                    return cb.isEmpty((Expression<? extends Collection<?>>) path);
                case ISNOTEMPTY:
                    return cb.isNotEmpty((Expression<? extends Collection<?>>) path);
                case IN:
                    return path.in((Collection<?>) value);
                case NOTIN:
                    return path.in((Collection<?>) value).not();
                default:
                    return null;
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Predicate parseGE(CriteriaBuilder cb, Object value, Path<?> path) {
        if (value instanceof Date) {
            return cb.greaterThanOrEqualTo((Expression<Date>) path, (Date) value);
        }
        if (value instanceof String) {
            return cb.greaterThanOrEqualTo((Expression<String>) path, (String) value);
        }
        return cb.ge((Expression<Number>) path, (Number) value);
    }

    @SuppressWarnings("unchecked")
    private Predicate parseGT(CriteriaBuilder cb, Object value, Path<?> path) {
        if (value instanceof Date) {
            return cb.greaterThan((Expression<Date>) path, (Date) value);
        }
        if (value instanceof String) {
            return cb.greaterThan((Expression<String>) path, (String) value);
        }
        return cb.gt((Expression<Number>) path, (Number) value);
    }

    @SuppressWarnings("unchecked")
    private Predicate parseLE(CriteriaBuilder cb, Object value, Path<?> path) {
        if (value instanceof Date) {
            return cb.lessThanOrEqualTo((Expression<Date>) path, (Date) value);
        }
        if (value instanceof String) {
            return cb.lessThanOrEqualTo((Expression<String>) path, (String) value);
        }
        return cb.le((Expression<Number>) path, (Number) value);
    }

    @SuppressWarnings("unchecked")
    private Predicate parseLT(CriteriaBuilder cb, Object value, Path<?> path) {
        if (value instanceof Date) {
            return cb.lessThan((Expression<Date>) path, (Date) value);
        }
        if (value instanceof String) {
            return cb.lessThan((Expression<String>) path, (String) value);
        }
        return cb.lt((Expression<Number>) path, (Number) value);
    }

    private Path<?> getPath(Root<T> root, String property) {
        Path<?> path = null;
        if (property.contains(".")) {
            String[] s = property.split("\\.");
            path = root.get(s[0]);
            for (int i = 1; i < s.length; i++) {
                path = path.get(s[i]);
            }
        } else {
            path = root.get(property);
        }
        return path;
    }
}
