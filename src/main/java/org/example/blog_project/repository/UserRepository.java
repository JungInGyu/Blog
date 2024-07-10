package org.example.blog_project.repository;

import org.example.blog_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    boolean existsByUid(String uid);

    User findByUid(String uid);

}
