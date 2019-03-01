package com.jee.learn.interfaces.support.jpa.qbc;

/**
 * 计算用类
 * 
 * @author https://github.com/344399160/JPAManager
 */
public class Projections {

    public static Projection Max(String col) {
        return new Projection(col, Criterion.Projection.MAX);
    }

    public static Projection Length(String col) {
        return new Projection(col, Criterion.Projection.LENGTH);
    }

    public static Projection Min(String col) {
        return new Projection(col, Criterion.Projection.MIN);
    }

    public static Projection Sum(String col) {
        return new Projection(col, Criterion.Projection.SUM);
    }

}
