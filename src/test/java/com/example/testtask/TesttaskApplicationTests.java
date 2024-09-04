package com.example.testtask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TesttaskApplicationTests {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DocumentManager documentManager;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private DocumentRepository documentRepository;

	@Test
	void contextLoads() {
		System.out.println(context.getBean(DocumentManager.class));
	}

	@Test
	void saveDocument() {
		var author = authorRepository.save(new DocumentManager.Author("11", "danya"));
		var document = documentManager.save(new DocumentManager.Document("1",
				"lol",
				"lol",
				author,
				Instant.now()));
		document = documentManager.save(new DocumentManager.Document("1",
				"olo",
				"olo",
				author,
				Instant.now()));
		System.out.println(documentRepository.findById("1").toString());
	}

	@Test
	void findByIdDocument() {
		documentManager.findById("1");
	}

	@Test
	void searchDocument() {
		var start = Instant.now();
		for(int i = 1; i <= 10; i++){
			var author = authorRepository.save(new DocumentManager.Author(String.valueOf(i), String.valueOf(i)));
			documentManager.save(new DocumentManager.Document(String.valueOf(i),
					String.valueOf(i * i),
					String.valueOf(i * i * 10),
					author,
					Instant.now()));
		}
		var list = new ArrayList<>(List.of("1"));
		var request = new DocumentManager.SearchRequest(new ArrayList<>(List.of("1")),
				new ArrayList<>(List.of("1")),
				new ArrayList<>(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")),
				start,
				Instant.now());
		var documentList = documentManager.search(request);
		for (DocumentManager.Document document: documentList) {
			System.out.println(document.toString());
		}
	}
}
