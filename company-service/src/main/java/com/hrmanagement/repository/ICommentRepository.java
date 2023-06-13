package com.hrmanagement.repository;

import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.enums.ECommentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ICommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByCompanyId(String companyId);

    List<Comment> findByECommentStatus(String status);

    List<Comment> findByCompanyId(String commentStatus,String companyId);
}
