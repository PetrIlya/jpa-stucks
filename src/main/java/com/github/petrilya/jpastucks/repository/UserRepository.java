package com.github.petrilya.jpastucks.repository;

import com.github.petrilya.jpastucks.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, String> {

}
