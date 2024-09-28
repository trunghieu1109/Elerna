package com.application.elerna.repository;

import com.application.elerna.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    public Optional<Lesson> findByName(String name);

}
