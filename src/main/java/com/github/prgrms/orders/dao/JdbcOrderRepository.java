package com.github.prgrms.orders.dao;

import com.github.prgrms.configures.web.SimplePageRequest;
import com.github.prgrms.orders.domain.Order;
import com.github.prgrms.orders.domain.Review;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcOrderRepository(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("orders")
                                                          .usingGeneratedKeyColumns("seq");
    }

    @Override
    public Optional<Order> findById(long id, long userId) {
        try {
            return Optional.of(select(id, userId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll(long userId, SimplePageRequest request) {
        String sql = "SELECT o.seq, o.user_seq, o.product_seq, o.review_seq, o.state, o.request_msg, o.reject_msg, o.completed_at, o.rejected_at, o.create_at, " +
                "r.content AS review_content, r.create_at AS review_create_at " +
                "FROM orders o LEFT JOIN reviews r ON o.review_seq = r.seq WHERE o.user_seq = (:user_seq) " +
                "ORDER BY o.seq DESC " +
                "LIMIT (:limit) OFFSET (:offset) ";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("user_seq", userId)
                                                                         .addValue("limit", request.getSize())
                                                                         .addValue("offset", request.getOffset());
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private Order select(long id, long userId) {
        final String sql = "SELECT o.seq, o.user_seq, o.product_seq, o.review_seq, o.state, o.request_msg, o.reject_msg, o.completed_at, o.rejected_at, o.create_at, " +
                "r.content AS review_content, r.create_at AS review_create_at " +
                "FROM orders o LEFT JOIN reviews r ON o.review_seq = r.seq WHERE o.seq = (:seq) AND o.user_seq = (:user_seq) ";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("seq", id)
                                                                         .addValue("user_seq", userId);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private Order toEntity(final ResultSet resultSet) throws SQLException {
        final Order order = new Order();
        order.setSeq(resultSet.getLong("seq"));
        order.setUserId(resultSet.getLong("user_seq"));
        order.setProductId(resultSet.getLong("product_seq"));
        order.setState(resultSet.getString("state"));
        order.setRequestMessage(resultSet.getString("request_msg"));
        order.setRejectMessage(resultSet.getString("reject_msg"));
        order.setCompletedAt(dateTimeOf(resultSet.getTimestamp("completed_at")));
        order.setRejectedAt(dateTimeOf(resultSet.getTimestamp("rejected_at")));
        order.setCreateAt(dateTimeOf(resultSet.getTimestamp("create_at")));

        if (resultSet.getLong("review_seq") > 0) {
            final Review review = new Review();
            review.setSeq(resultSet.getLong("review_seq"));
            review.setUserSeq(resultSet.getLong("user_seq"));
            review.setProductSeq(resultSet.getLong("product_seq"));
            review.setContent(resultSet.getString("review_content"));
            review.setCreateAt(dateTimeOf(resultSet.getTimestamp("review_create_at")));
            order.setReview(review);
        }
        return order;
    }

    @Override
    public void update(Order order) {
        final String sql = "UPDATE orders SET state = (:state), review_seq = (:review_seq), reject_msg = (:reject_msg), rejected_at = (:rejected_at), completed_at = (:completed_at) " +
                "WHERE seq = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("state", order.getState())
                                                                         .addValue("review_seq", order.getReview() == null ? null : order.getReview().getSeq())
                                                                         .addValue("reject_msg", order.getRequestMessage())
                                                                         .addValue("rejected_at", order.getRejectedAt())
                                                                         .addValue("completed_at", order.getCompletedAt())
                                                                         .addValue("id", order.getSeq());
        jdbcTemplate.update(sql, parameters);
    }
}
