package com.redis.all.dbops.service;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import com.redis.all.dbops.model.MyDocument;
import com.redis.all.dbops.repo.MyDocumentRepository;
import java.util.Optional;

@Service
public class MyDocumentService {

    private MyDocumentRepository repository;
    public MyDocumentService(MyDocumentRepository repository) {
        this.repository = repository;
    }

    public MyDocument saveDocument(MyDocument document) {
        return repository.save(document);
    }

    public MyDocument getDocumentById(String id) {
        return repository.findById(id).orElse(null);
    }
    public MyDocument updateDocument(String content, String id) {
        Optional<MyDocument> documentOptional = repository.findById(id);
        if(documentOptional.isPresent() && !StringUtil.isNullOrEmpty(content)) {
            MyDocument document = documentOptional.get();
            document.setContent(content);
            return repository.save(document);
        } else return null;
    }

    public void deleteDocument(String id) {
        repository.deleteById(id);
    }
}
