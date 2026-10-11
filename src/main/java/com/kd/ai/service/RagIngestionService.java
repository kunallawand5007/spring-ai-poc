package com.kd.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class RagIngestionService {

    private final VectorStore vectorStore;


    public RagIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> uploadVector(MultipartFile file) {

        log.info("Uploading file...{}",file.getName());

        try {

            PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(file.getResource(),
                    PdfDocumentReaderConfig.builder()
                            .withPageTopMargin(0)
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                    .withNumberOfTopTextLinesToDelete(0)
                                    .build())
                            .withPagesPerDocument(1)
                            .build());

            List<Document> documents =
                    pagePdfDocumentReader.get();

            vectorStore.add(documents);

            log.info("embedding saved...!!!");

            return documents;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    //https://docs.spring.io/spring-ai/reference/api/vectordbs.html#_writing_to_a_vector_store
}
