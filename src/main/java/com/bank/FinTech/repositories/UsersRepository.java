package com.bank.FinTech.repositories;


import com.bank.FinTech.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findByBvn(String bvn);
    Optional<Users> findUsersByEmail (String email);

}
