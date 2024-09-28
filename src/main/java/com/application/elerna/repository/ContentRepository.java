package com.application.elerna.repository;

import com.application.elerna.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    public Optional<Content> findByName(String name);

}
