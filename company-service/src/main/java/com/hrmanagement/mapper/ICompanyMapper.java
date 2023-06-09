package com.hrmanagement.mapper;

import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.dto.response.CompanyDetailedInformationResponseDto;
import com.hrmanagement.dto.response.CompanyInformationResponseDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import com.hrmanagement.dto.response.VisitorCompanyInformations;
import com.hrmanagement.repository.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICompanyMapper {
    ICompanyMapper INSTANCE = Mappers.getMapper(ICompanyMapper.class);

    Company fromSaveCompanyResponseDtoToCompany(final SaveCompanyRequestDto dto);

    CompanyInformationResponseDto fromCompanyToCompanyInformationResponseDto(final Company company);

    VisitorCompanyInformations fromCompanyToVisitorCompanyInformations(final Company company);

    PersonnelCompanyInformationResponseDto fromCompanyToPersonnelCompanyInformationResponseDto(final Company company);

    CompanyDetailedInformationResponseDto fromCompanyToCompanyDetailedInformationResponseDto(final Company company);
}
