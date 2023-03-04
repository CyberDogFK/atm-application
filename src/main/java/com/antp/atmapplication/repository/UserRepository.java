package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @Query("select distinct u from User u "
            + "left join fetch u.accounts a"
    )
    List<User> findAll();

    @Override
    @Query("select distinct u from User u"
            + " left join fetch u.accounts"
            + " where u.id = ?1")
    Optional<User> findById(Long usedId);

    Optional<User> findUserByName(String name);
}
