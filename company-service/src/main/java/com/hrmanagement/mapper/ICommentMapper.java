package com.hrmanagement.mapper;

import com.hrmanagement.dto.response.FindCompanyCommentsResponseDto;
import com.hrmanagement.dto.response.PersonnelActiveCompanyCommentsResponseDto;
import com.hrmanagement.dto.response.UserProfileCommentResponseDto;
import com.hrmanagement.repository.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommentMapper {
    ICommentMapper INSTANCE = Mappers.getMapper(ICommentMapper.class);

    Comment fromUserProfileCommentResponseDtoToComment(final UserProfileCommentResponseDto dto);

    FindCompanyCommentsResponseDto fromCompanyToFindCompanyCommentsResponseDto(final Comment comment);

    PersonnelActiveCompanyCommentsResponseDto fromCommentToPersonnelActiveCompanyCommentsResponseDto(final Comment comment);

}
