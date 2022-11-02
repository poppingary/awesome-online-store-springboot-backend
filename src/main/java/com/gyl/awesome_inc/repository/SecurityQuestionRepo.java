package com.gyl.awesome_inc.repository;

import com.gyl.awesome_inc.domain.model.SecurityQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityQuestionRepo extends CrudRepository<SecurityQuestion, String> {
    Optional<SecurityQuestion> findBySecurityQuestion(String securityQuestion);
}