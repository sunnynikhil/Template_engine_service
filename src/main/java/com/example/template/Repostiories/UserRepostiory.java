package com.example.template.Repostiories;

import com.example.template.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "UserRepostiory")
public interface UserRepostiory extends JpaRepository<User , Integer> {


    @Query("FROM User u where u.id= :id ")
    User findById(@Param("id") int id);
}
