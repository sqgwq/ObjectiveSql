package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.annotations.Volatile;
import com.github.braisdom.funcsql.relation.RelationType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public final class Domains {

    private Domains(){}

    @DomainModel
    public static class Member {
        private Long id;
        private String no;
        private String name;
        private int gender;
        private String mobile;

        @Column(transition = JsonColumnTransition.class)
        private Map extendedAttributes;

        @Volatile
        private String otherInfo;
    }

    @DomainModel
    public static class Order {
        private Long id;
        private String no;
        private long memberId;
        private float amount;
        private float quantity;
        private Time salesAt;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Member member;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<OrderLine> orderLines;
    }

    @DomainModel
    public static class OrderLine {
        private Long id;
        private String orderNo;
        private float amount;
        private float quantity;

        @Relation(relationType = RelationType.BELONGS_TO)
        private Order order;
    }

    public static void createTables(Connection connection) throws SQLException {
        connection.createStatement().execute("drop table if exists members;");
        connection.createStatement().execute("drop table if exists orders");
        connection.createStatement().execute("drop table if exists order_lines");

        connection.createStatement().execute("create table members (id INTEGER, no TEXT, " +
                "name TEXT, gender INTEGER, mobile TEXT, extended_attributes TEXT)");
        connection.createStatement().execute("create table orders (id INTEGER, no TEXT, member_id INTEGER, " +
                "amount REAL, quantity REAL, sales_at TEXT)");
        connection.createStatement().execute("create table order_lines (id integer, order_no TEXT, amount REAL, quantity REAL)");
    }
}
