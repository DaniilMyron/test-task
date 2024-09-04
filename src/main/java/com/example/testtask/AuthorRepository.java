package com.example.testtask;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<DocumentManager.Author, String> {
}
