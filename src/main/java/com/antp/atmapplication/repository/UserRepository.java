package com.antp.atmapplication.repository;

import com.antp.atmapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @NonNull
    @Query("select distinct u from User u "
            + "left join fetch u.accounts "
            + "left join fetch u.role"
    )
    List<User> findAll();

    @Override
    @NonNull
    @Query("select distinct u from User u "
            + "left join fetch u.accounts "
            + "left join fetch u.role "
            + "where u.id = ?1")
    Optional<User> findById(@NonNull Long usedId);

    @Query("select distinct u from User u "
            + "left join fetch u.accounts "
            + "left join fetch u.role "
            + "where u.name = ?1")
    Optional<User> findUserByName(String name);
}
