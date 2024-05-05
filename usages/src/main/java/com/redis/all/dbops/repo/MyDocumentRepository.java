package com.redis.all.dbops.repo;


import com.redis.all.dbops.model.MyDocument;
import org.springframework.data.repository.CrudRepository;

public interface MyDocumentRepository extends CrudRepository<MyDocument, String> {
}
