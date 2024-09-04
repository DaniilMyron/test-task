package com.example.testtask;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentManager.Document, String> {
    List<DocumentManager.Document> findByTitleStartingWith(String titlePrefix);
    List<DocumentManager.Document> findByContentContaining(String contentContaining);
    List<DocumentManager.Document> findByAuthorId(String authorId);
    List<DocumentManager.Document> findByCreatedAfter(Instant createdFrom);
    List<DocumentManager.Document> findByCreatedBefore(Instant createdTo);
}
