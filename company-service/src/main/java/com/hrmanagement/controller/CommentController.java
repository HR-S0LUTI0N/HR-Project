package com.hrmanagement.controller;

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

    @PostMapping("/personnel-make-comment/{token}")
    public ResponseEntity<Boolean> personnelMakeComment(@PathVariable String token, @RequestBody PersonnelCommentRequestDto dto){
        return ResponseEntity.ok(commentService.personnelMakeComment(token,dto));
    }
}
