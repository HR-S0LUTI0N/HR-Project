package com.hrmanagement.service;

import com.hrmanagement.dto.request.ChangeCommentStatusRequestDto;
import com.hrmanagement.dto.request.PersonnelCommentRequestDto;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICommentMapper;
import com.hrmanagement.repository.ICommentRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.enums.ECommentStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceManager<Comment, String> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;

    public CommentService(ICommentRepository commentRepository,
                          JwtTokenProvider jwtTokenProvider,
                          IUserManager userManager) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
    }

    public Boolean personnelMakeComment(String token, PersonnelCommentRequestDto dto) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        if (roles.contains(ERole.PERSONEL.toString())) {
            UserProfileCommentResponseDto userProfileCommentResponseDto = userManager.getUserProfileCommentInformation(authId).getBody();
            Comment comment = ICommentMapper.INSTANCE.fromUserProfileCommentResponseDtoToComment(userProfileCommentResponseDto);
            comment.setComment(dto.getComment());
            save(comment);
            return true;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public List<FindCompanyCommentsResponseDto> findCompanyComments(String companyId) {
        List<Comment> commentList = commentRepository.findByCompanyId(companyId);
        List<FindCompanyCommentsResponseDto> companyComments = commentList.stream().filter(y ->
                y.getECommentStatus() == ECommentStatus.ACTIVE).map(x ->
                ICommentMapper.INSTANCE.fromCompanyToFindCompanyCommentsResponseDto(x)).collect(Collectors.toList());
        return companyComments;
    }


    public Boolean changeCommentStatus(String token, ChangeCommentStatusRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        if (roles.contains(ERole.ADMIN.toString())) {
            Comment comment = findById(dto.getCommentId()).orElseThrow(() -> {
                throw new CompanyManagerException(ErrorType.COMMENT_NOT_FOUND);
            });
            if (comment.getECommentStatus() == ECommentStatus.PENDING) {
                if (dto.getAction()) {
                    comment.setECommentStatus(ECommentStatus.ACTIVE);
                } else {
                    comment.setECommentStatus(ECommentStatus.DELETED);
                }
                update(comment);
                return true;
            }
            throw new CompanyManagerException(ErrorType.COMMENT_NOT_PENDING);
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public List<Comment> findByCommentByStatus() {
        List<Comment> commentList = commentRepository.findAll();
        List<Comment> pendingComment = new ArrayList<>();
        commentList.forEach(x -> {
            if (x.getECommentStatus() == ECommentStatus.PENDING) {
                pendingComment.add(x);
            }
        });
        return pendingComment;
    }

    public List<PersonnelActiveCompanyCommentsResponseDto> findAllActiveCompanyComments(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.PERSONEL.toString())) {
            PersonnelDashboardCommentResponseDto dto = userManager.findAllActiveCompanyComments(authId).getBody();
            List<Comment> commentList = commentRepository.findByCompanyId(dto.getCompanyId());
            System.out.println(commentList);
            List<Comment> filteredComments= new ArrayList<>();
                    commentList.stream().forEach(x -> {
                if(x.getECommentStatus() == ECommentStatus.ACTIVE)
                    filteredComments.add(x);
            });
            List<PersonnelActiveCompanyCommentsResponseDto> activeCompanyCommentsResponseDtos =
                    filteredComments.stream().map(comment -> {
                        PersonnelActiveCompanyCommentsResponseDto dto1 = ICommentMapper.INSTANCE.fromCommentToPersonnelActiveCompanyCommentsResponseDto(comment);
                        String avatar = userManager.getUserAvatarByUserId(comment.getUserId()).getBody();
                        dto1.setAvatar(avatar);
                        return dto1;
                    }).collect(Collectors.toList());
            System.out.println(activeCompanyCommentsResponseDtos);
            return activeCompanyCommentsResponseDtos;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);

    }







}
