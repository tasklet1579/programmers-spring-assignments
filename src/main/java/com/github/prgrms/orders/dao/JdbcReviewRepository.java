package com.github.prgrms.orders.dao;

import com.github.prgrms.orders.domain.Review;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;

@Repository
public class JdbcReviewRepository implements ReviewRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReviewRepository(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("reviews")
                                                          .usingGeneratedKeyColumns("seq");
    }

    @Override
    public Review save(Review review) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(review);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private Review select(final Long id) {
        final String sql = "SELECT seq, user_seq, product_seq, content, create_at FROM reviews WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private Review toEntity(final ResultSet resultSet) throws SQLException {
        final Review entity = new Review();
        entity.setSeq(resultSet.getLong("seq"));
        entity.setUserSeq(resultSet.getLong("user_seq"));
        entity.setProductSeq(resultSet.getLong("product_seq"));
        entity.setContent(resultSet.getString("content"));
        entity.setCreateAt(dateTimeOf(resultSet.getTimestamp("create_at")));
        return entity;
    }
}
