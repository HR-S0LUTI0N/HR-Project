package com.hrmanagement.service;


import com.hrmanagement.dto.request.PersonelExpenseRequestDto;
import com.hrmanagement.dto.response.UserProfileExpenseResponseDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICommentMapper;
import com.hrmanagement.mapper.IExpenseMapper;
import com.hrmanagement.repository.IExpenseRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.Expense;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService extends ServiceManager<Expense, String> {

    private final IExpenseRepository expenseRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;

    public ExpenseService( IExpenseRepository expenseRepository, JwtTokenProvider jwtTokenProvider, IUserManager userManager) {
        super(expenseRepository);
        this.expenseRepository = expenseRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
    }

    public Boolean personnelMakeExpense(String token, PersonelExpenseRequestDto personelExpenseRequestDto) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        if (roles.contains(ERole.PERSONEL.toString())) {
            UserProfileExpenseResponseDto userProfileExpenseResponseDto = userManager.getUserProfileExpenseInformation(authId).getBody();
            Expense expense = IExpenseMapper.INSTANCE.fromPersonelExpenseRequestDtoToExpense(personelExpenseRequestDto);
            expense.setUserId(userProfileExpenseResponseDto.getUserId());
            expense.setName(userProfileExpenseResponseDto.getName());
            expense.setSurname(userProfileExpenseResponseDto.getSurname());
            expense.setCompanyId(userProfileExpenseResponseDto.getCompanyId());
            save(expense);
            return true;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

}
