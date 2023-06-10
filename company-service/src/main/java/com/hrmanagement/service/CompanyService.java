package com.hrmanagement.service;

import com.hrmanagement.dto.response.*;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICompanyMapper;
import com.hrmanagement.repository.ICompanyRepository;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService extends ServiceManager<Company,String> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final CommentService commentService;
    private CompanyService(ICompanyRepository companyRepository,
                           JwtTokenProvider jwtTokenProvider,
                           IUserManager userManager,
                           CommentService commentService){
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.commentService = commentService;
    }

    public Boolean save(SaveCompanyRequestDto dto){
        if(!companyRepository.existsByCompanyNameIgnoreCase(dto.getCompanyName())){
            Company company = ICompanyMapper.INSTANCE.fromSaveCompanyResponseDtoToCompany(dto);
            save(company);
            return true;
        }
        throw new CompanyManagerException(ErrorType.COMPANY_ALREADY_EXIST);
    }

    //İlgili müdür için hazırlanan şirket bilgileri getir metodudur. Role'le kontrol ypaılacak
    public CompanyInformationResponseDto showCompanyInformation(String token){
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        String companyId = userManager.getCompanyId(authId.get()).getBody();
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        return ICompanyMapper.INSTANCE.fromCompanyToCompanyInformationResponseDto(company);
    }

    //Tüm companylerin preview bilgileri için metot
    public List<VisitorCompanyInformations> findAllCompanyPreviewInformation(){
        List<Company> companyList = companyRepository.findAll();
        List<VisitorCompanyInformations> companyInformationsList = new ArrayList<>();
        companyList.forEach(company -> {
            VisitorCompanyInformations dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorCompanyInformations(company);
            companyInformationsList.add(dto);
        });
        return companyInformationsList;
    }

    //Detaylı company sayfası için metot
    public VisitorDetailedCompanyInformationResponse findCompanyDetailedInformation(String companyId){
        Company company = findById(companyId).orElseThrow(()->{throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);});
        System.out.println(company);
        VisitorDetailedCompanyInformationResponse dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorDetailedCompanyInformationResponse(company);
        System.out.println(dto);

        List<FindCompanyCommentsResponseDto> dtoCommentList = commentService.findCompanyComments(companyId);
        System.out.println(dtoCommentList);
        dto.setCompanyComments(dtoCommentList);
        return dto;
    }


    public PersonnelCompanyInformationResponseDto getPersonnelCompanyInformation(String companyId) {
        Company company = findById(companyId).orElseThrow(()->{throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);});
        return ICompanyMapper.INSTANCE.fromCompanyToPersonnelCompanyInformationResponseDto(company);
    }

    public Boolean doesCompanyIdExist(String companyId){
        if(companyRepository.existsByCompanyId(companyId)){
            return true;
        }
        return false;
    }
}
