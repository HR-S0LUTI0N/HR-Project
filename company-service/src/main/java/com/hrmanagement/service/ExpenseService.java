package com.hrmanagement.service;


import com.hrmanagement.dto.request.ChangeExpenseStatusRequestDto;
import com.hrmanagement.dto.request.PersonelExpenseRequestDto;
import com.hrmanagement.dto.response.CompanyExpenseListResponseDto;
import com.hrmanagement.dto.response.UserProfileExpenseResponseDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICommentMapper;
import com.hrmanagement.mapper.IExpenseMapper;
import com.hrmanagement.repository.IExpenseRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.Expense;
import com.hrmanagement.repository.entity.enums.ECommentStatus;
import com.hrmanagement.repository.entity.enums.EExpenseStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<CompanyExpenseListResponseDto> findAllCompanyExpenseList(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        if(roles.contains(ERole.MANAGER.toString())){
            String companyId = userManager.findUserCompanyId(authId).getBody();
            List<Expense> expenseList = expenseRepository.findAllByCompanyId(companyId);
            if(expenseList.isEmpty())
                throw new CompanyManagerException(ErrorType.NO_EXPENSE_EXIST);
            List<CompanyExpenseListResponseDto> dtoList = expenseList.stream().filter(expense ->
                expense.getEExpenseStatus().equals(EExpenseStatus.PENDING)
                    ).map(expense->{
                    CompanyExpenseListResponseDto dto = IExpenseMapper.INSTANCE.fromExpenseToCompanyExpenseListResponseDto(expense);
                    dto.setEExpenseStatus(expense.getEExpenseStatus());
                    String avatar = userManager.findAvatar(expense.getUserId()).getBody();
                    dto.setAvatar(avatar);
                    if(expense.getBillPhoto()!=null){
                        try{
                            byte[] decodedBytes = Base64.getDecoder().decode(expense.getBillPhoto());
                            String decodedPhoto = new String(decodedBytes);
                            dto.setRecipePhoto(decodedPhoto);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    return dto;

            }
                    )
                    .collect(Collectors.toList());
            return dtoList;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public Boolean changeExpenseStatus(String token, ChangeExpenseStatusRequestDto dto){
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{
            throw new CompanyManagerException(ErrorType.NO_EXPENSE_EXIST);
        });
        UserProfileExpenseResponseDto userProfileExpenseResponseDto = userManager.getUserProfileExpenseInformation(authId).getBody();
        if (roles.contains(ERole.MANAGER.toString())) {
            System.out.println(userProfileExpenseResponseDto);
            System.out.println(dto);
            Expense expense = expenseRepository.findByCompanyIdAndExpenseId(userProfileExpenseResponseDto.getCompanyId(),dto.getExpenseId())
                    .orElseThrow(() -> {
                throw new CompanyManagerException(ErrorType.NO_EXPENSE_EXIST);
            });
            System.out.println(expense);
            if (expense.getEExpenseStatus() == EExpenseStatus.PENDING) {
                if (dto.getAction()) {
                    expense.setEExpenseStatus(EExpenseStatus.ACTIVE);
                } else {
                    expense.setEExpenseStatus(EExpenseStatus.DECLINED);
                }
                update(expense);
                return true;
            }
            throw new CompanyManagerException(ErrorType.EXPENSE_NOT_PENDING);
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

}
