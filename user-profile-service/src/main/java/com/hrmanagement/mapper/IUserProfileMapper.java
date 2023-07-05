package com.hrmanagement.mapper;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.rabbitmq.model.PersonnelPasswordModel;
import com.hrmanagement.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {

    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);


    UserProfile fromCreateUserProfileRequestDtoToUserProfile(final CreateUserProfileRequestDto dto);

    AuthCreatePersonnelProfileRequestDto fromUserProfileToAuthCreatePersonelProfileRequestDto(final UserProfile userProfile);

    PersonnelPasswordModel fromUserProfileToPersonnelPasswordModel(final UserProfile userProfile);

    UserProfile fromNewCreateVisitorUserResponseDtoToUserProfile(final NewCreateVisitorUserResponseDto dto);

    UserProfile fromNewCreateManagerUserResponseDtoToUserProfile(final NewCreateManagerUserResponseDto dto);

    SaveCompanyRequestDto fromNewCreateManagerUserResponseDtoToSaveCompanyRequestDto (final NewCreateManagerUserResponseDto dto);

    UpdateManagerStatusRequestDto fromUserProfileToUpdateManagerStatusRequestDto(final UserProfile userProfile);

    DeletePersonnelFromAuthRequestDto fromUserProfileToDeletePersonnelFromAuthRequestDto(final UserProfile userProfile);

    PersonnelInformationResponseDto fromUserProfileToPersonnelInformationResponseDto(final UserProfile userProfile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonnelInformationResponseDto fromPersonnelCompanyInformationResponseDtoToPersonnelInformationResponseDto(PersonnelCompanyInformationResponseDto dto,
                                                                                                                @MappingTarget PersonnelInformationResponseDto dtoMain);
    UserProfileCommentResponseDto fromUserProfileToUserProfileCommentResponseDto(final UserProfile userProfile);

    UserProfileExpenseResponseDto fromUserProfileToUserProfileExpenseResponseDto(final UserProfile userProfile);

    UserProfilePersonnelDashboardRequestDto fromUserProfileToUserProfilePersonnelDashboardRequestDto(final UserProfile userProfile);

    PersonnelDashboardCommentRequestDto fromUserProfileToPersonnelDashboardCommentRequestDto(final UserProfile userProfile);

    PersonnelProfilesForManagerDashBoardResponseDto fromUserProfileToPersonnelProfilesForManagerDashBoardResponseDto(final UserProfile userProfile);



    UserProfileAvatarAndNameResponseDto fromUserProfileToUserProfileAvatarAndNameResponseDto(final UserProfile userProfile);
    UserProfileAvatarAndNameAndSurnameResponseDto fromUserProfileToUserProfileAvatarAndNameAndSurnameResponseDto(final UserProfile userProfile);

    UserProfileSendingInfosResponseDto userProfileToUserProfileSendingInfosResponseDto(final UserProfile userProfile);
    UserProfileSendingInfosResponseDto companyInfosToUserProfileSendingInfosResponseDto(final AllCompanyInfosForUserProfileResponseDto dto);
    PersonelUpdateUserProfileToAuthRequestDto fromPersonelUpdateRequestDtoToPersonelUpdateUserProfileToAuthRequestDto (final PersonelUpdateRequestDto dto);
    ToAuthPasswordChangeDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);
}
