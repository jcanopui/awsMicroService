package com.zurich.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.zurich.register.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Token findByUserIdAndId(String userId, Long id);

	List<Token> findByUserId(String userId);
}