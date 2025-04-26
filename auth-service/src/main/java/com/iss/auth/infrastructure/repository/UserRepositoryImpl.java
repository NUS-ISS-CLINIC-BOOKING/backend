package com.iss.auth.infrastructure.repository;
import com.iss.auth.domain.entity.User;
import com.iss.auth.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByEmail(String email) {
        // TODO: 这里连数据库查询，比如 SELECT * FROM user WHERE email = ?
        // 暂时举例，实际应该接数据库
        if ("test@example.com".equals(email)) {
            return new User(1L, "James", "test@example.com", "password123", null); // 手写一条
        }
        return null;
    }

    @Override
    public void save(User user) {
        // TODO: 这里是插入到数据库
        System.out.println("Save user: " + user.getEmail());
    }
}
