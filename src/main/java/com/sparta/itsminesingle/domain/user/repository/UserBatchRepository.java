package com.sparta.itsminesingle.domain.user.repository;

import com.sparta.itsminesingle.domain.product.entity.Product;
import com.sparta.itsminesingle.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserBatchRepository {

    private static final Logger log = LoggerFactory.getLogger(UserBatchRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private static final int BATCH_SIZE = 10000;

    public void insert(List<User> userList) {
        String sql =
                "INSERT INTO USER (username, password, name, nickname, email, address, user_Role)"
                        + "VALUES (?,?,?,?,?,?,?)";

        for (int i = 0; i < userList.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, userList.size());
            List<User> batch = userList.subList(i, end);

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    User user = batch.get(i);
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getName());
                    ps.setString(4, user.getNickname());
                    ps.setString(5, user.getEmail());
                    ps.setString(6, user.getAddress());
                    ps.setString(7, user.getUserRole().name());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
            log.info("{}번까지 데이터 insert", end);
        }
    }
}
