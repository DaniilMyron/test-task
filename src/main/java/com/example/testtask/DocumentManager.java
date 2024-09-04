package com.example.testtask;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
@Service
public class DocumentManager {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        List<Document> documents = new ArrayList<>();
        List<Document> tempList = new ArrayList<>();
        for (String titlePrefix : request.titlePrefixes) {
            documents.addAll(documentRepository.findByTitleStartingWith(titlePrefix));
        }
        for (String containsContents : request.containsContents) {
            tempList.addAll(documentRepository.findByContentContaining(containsContents));
        }
        documents.retainAll(tempList);
        tempList.clear();
        for (String authorIds : request.authorIds) {
            tempList.addAll(documentRepository.findByAuthorId(authorIds));
        }
        retrainFromTempList(tempList, documents);

        tempList.addAll(documentRepository.findByCreatedAfter(request.createdFrom));
        retrainFromTempList(tempList, documents);

        tempList.addAll(documentRepository.findByCreatedBefore(request.createdTo));
        retrainFromTempList(tempList, documents);

        return documents;
    }

    private void retrainFromTempList(List<Document> tempList, List<Document> documents){
        documents.retainAll(tempList);
        tempList.clear();
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return documentRepository.findById(id);
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Document {
        @Id
        private String id;
        private String title;
        private String content;
        @OneToOne
        @JoinColumn(name = "author_id")
        private Author author;
        private Instant created;

        public String toString(){
            return id + " " + title + " " + content + " " + author.id + " " + created;
        }
    }

    @Data
    @Builder
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {
        @Id
        private String id;
        private String name;
    }
}
