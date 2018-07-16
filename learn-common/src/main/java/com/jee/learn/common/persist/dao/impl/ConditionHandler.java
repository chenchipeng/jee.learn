package com.jee.learn.common.persist.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.dao.Sort;
import com.jee.learn.common.persist.dao.Sort.Direction;


/**
 * 
 * <p>Title: ConditionHandler</p>
 * <p>Description: 查询处理器</p>
 * <p>Copyright: Copyright (c) 2013 ITDCL  All right reserved.</p>
 * <p>Company: ITDCL</p>
 * @author yjf
 * @version 1.0
 *
 * 修改记录:
 * 下面填写修改的内容以及修改的日期
 * 1.2013-9-6 下午5:26:34  yjf    new
 */
public abstract class ConditionHandler<T> {

	/**
	 * 解释查询条件
	 * 
	 * @param root
	 * @param query
	 * @param cb
	 * @param con
	 * @return
	 */
	protected abstract CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con,
			Sort sort);

	@SuppressWarnings("unchecked")
	protected Predicate[] parseCondition(Root<T> root, CriteriaBuilder cb, Condition con) {
		List<Predicate> list = new ArrayList<Predicate>();
		for (Condition.Paramerter p : con) {
			Path<?> path = getPath(root, p.getFieldName());
			switch (p.getOperator()) {
			case EQ:
				list.add(cb.equal(path, p.getValue()));
				break;
			case NE:
				list.add(cb.notEqual(path, p.getValue()));
				break;
			case LIKE:
				if (("" + p.getValue()).contains("%")) {
					list.add(cb.like((Expression<String>) path, "" + p.getValue()));
				} else {
					list.add(cb.like((Expression<String>) path, "%" + p.getValue() + "%"));
				}
				break;
			case GE:
				list.add(parseGE(cb, p.getValue(), path));
				break;
			case GT:
				list.add(parseGT(cb, p.getValue(), path));
				break;
			case LE:
				list.add(parseLE(cb, p.getValue(), path));
				break;
			case LT:
				list.add(parseLT(cb, p.getValue(), path));
				break;
			case ISNULL:
				list.add(cb.isNull(path));
				break;
			case ISNOTNULL:
				list.add(cb.isNotNull(path));
				break;
			case ISEMPTY:
				list.add(cb.isEmpty((Expression<? extends Collection<?>>) path));
				break;
			case ISNOTEMPTY:
				list.add(cb.isNotEmpty((Expression<? extends Collection<?>>) path));
				break;
			case IN:
				list.add(path.in((Collection<?>) p.getValue()));
				break;
			case NOTIN:
				list.add(path.in((Collection<?>) p.getValue()).not());
				break;
			case OR:
				Condition subCon = (Condition) p.getValue();
				if (subCon != null) {
					Predicate[] pres = parseCondition(root, cb, subCon);
					if (pres != null && pres.length > 0) {
						list.add(cb.or(pres));
					}
				}
				break;
			default:
				break;
			}
		}
		return list.toArray(new Predicate[list.size()]);
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

	protected List<Order> parseSort(Root<T> root, CriteriaBuilder cb, Sort sort) {
		List<Order> orders = new ArrayList<Order>();
		for (Sort.Order o : sort) {
			if (o.getDirection() == Direction.DESC) {
				orders.add(cb.desc(getPath(root, o.getProperty())));
			} else if (o.getDirection() == Direction.ASC) {
				orders.add(cb.asc(getPath(root, o.getProperty())));
			}
		}

		return orders;
	}
}
