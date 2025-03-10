package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
