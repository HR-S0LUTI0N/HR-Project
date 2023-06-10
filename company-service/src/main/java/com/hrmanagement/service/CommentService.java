package com.hrmanagement.service;

import com.hrmanagement.dto.request.PersonnelCommentRequestDto;
import com.hrmanagement.dto.response.FindCompanyCommentsResponseDto;
import com.hrmanagement.dto.response.UserProfileCommentResponseDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICommentMapper;
import com.hrmanagement.repository.ICommentRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceManager<Comment,String> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;

    public CommentService(ICommentRepository commentRepository,
                          JwtTokenProvider jwtTokenProvider,
                          IUserManager userManager){
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
    }

    public Boolean personnelMakeComment(String token, PersonnelCommentRequestDto dto){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        if(roles.contains(ERole.PERSONEL.toString())){
            UserProfileCommentResponseDto userProfileCommentResponseDto = userManager.getUserProfileCommentInformation(authId).getBody();
            Comment comment = ICommentMapper.INSTANCE.fromUserProfileCommentResponseDtoToComment(userProfileCommentResponseDto);
            comment.setComment(dto.getComment());
            save(comment);
            System.out.println(comment);
            return true;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public List<FindCompanyCommentsResponseDto> findCompanyComments(String companyId){
        List<Comment> commentList = commentRepository.findByCompanyId(companyId);
        if(commentList.isEmpty())
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        List<FindCompanyCommentsResponseDto> companyComments = commentList.stream().map(x ->
                ICommentMapper.INSTANCE.fromCompanyToFindCompanyCommentsResponseDto(x)).collect(Collectors.toList());
        return companyComments;
    }





}
