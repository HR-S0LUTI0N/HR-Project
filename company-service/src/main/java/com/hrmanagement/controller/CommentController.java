package com.hrmanagement.controller;

import com.hrmanagement.dto.request.ChangeCommentStatusRequestDto;
import com.hrmanagement.dto.request.PersonnelCommentRequestDto;
import com.hrmanagement.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hrmanagement.constants.ApiUrls.*;

@RestController
@RequestMapping(COMMENT)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/personnel-make-comment/{token}")
    public ResponseEntity<Boolean> personnelMakeComment(@PathVariable String token, @RequestBody PersonnelCommentRequestDto dto){
        return ResponseEntity.ok(commentService.personnelMakeComment(token,dto));
    }

    @PutMapping("/change-comment-status/{token}")
    public ResponseEntity<Boolean> changeCommentStatus(@PathVariable String token, @RequestBody ChangeCommentStatusRequestDto dto){
        return ResponseEntity.ok(commentService.changeCommentStatus(token,dto));
    }
}
