package com.hrmanagement.mapper;


import com.hrmanagement.dto.request.PersonelExpenseRequestDto;
import com.hrmanagement.dto.response.CompanyExpenseListResponseDto;
import com.hrmanagement.dto.response.UserProfileExpenseResponseDto;
import com.hrmanagement.repository.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IExpenseMapper {

    IExpenseMapper INSTANCE = Mappers.getMapper(IExpenseMapper.class);


    Expense fromUserProfileExpenseResponseDtoToExpense(final UserProfileExpenseResponseDto dto);

    Expense fromPersonelExpenseRequestDtoToExpense(final PersonelExpenseRequestDto dto);

    CompanyExpenseListResponseDto fromExpenseToCompanyExpenseListResponseDto(final Expense expense);
}
