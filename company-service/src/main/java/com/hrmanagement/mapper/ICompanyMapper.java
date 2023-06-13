package com.hrmanagement.mapper;

import com.hrmanagement.dto.request.FindPendingCommentWithCompanyName;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.repository.entity.Company;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);

    Company fromSaveCompanyResponseDtoToCompany(final SaveCompanyRequestDto dto);

    CompanyInformationResponseDto fromCompanyToCompanyInformationResponseDto(final Company company);

    VisitorCompanyInformations fromCompanyToVisitorCompanyInformations(final Company company);

    PersonnelCompanyInformationResponseDto fromCompanyToPersonnelCompanyInformationResponseDto(final Company company);

    VisitorDetailedCompanyInformationResponse fromCompanyToVisitorDetailedCompanyInformationResponse(final Company company);

}
