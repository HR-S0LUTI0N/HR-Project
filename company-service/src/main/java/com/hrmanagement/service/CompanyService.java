package com.hrmanagement.service;

import com.hrmanagement.dto.response.CompanyInformationResponseDto;
import com.hrmanagement.dto.response.PersonnelCompanyInformationResponseDto;
import com.hrmanagement.dto.response.SaveCompanyResponseDto;
import com.hrmanagement.dto.response.VisitorCompanyInformations;
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
    private CompanyService(ICompanyRepository companyRepository,
                           JwtTokenProvider jwtTokenProvider,
                           IUserManager userManager){
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
    }

    public String save(SaveCompanyResponseDto dto){
        Company company = ICompanyMapper.INSTANCE.fromSaveCompanyResponseDtoToCompany(dto);
        if(dto.getCompanyId()==null){
            save(company);
            return company.getCompanyId();
        }else{
            Optional<Company> optionalCompany = findById(dto.getCompanyId());
            if (optionalCompany.isPresent()) {
                return optionalCompany.get().getCompanyId();
            }else{
                company.setCompanyId(null);
                save(company);
                return company.getCompanyId();
            }
        }
    }


    public CompanyInformationResponseDto showCompanyInformation(String token){
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if(authId.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        String companyId = userManager.getCompanyId(authId.get()).getBody();
        System.out.println(companyId);
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        return ICompanyMapper.INSTANCE.fromCompanyToCompanyInformationResponseDto(company);
    }

    public List<VisitorCompanyInformations> findAllCompanyInformations(){
        List<Company> companyList = companyRepository.findAll();
        List<VisitorCompanyInformations> companyInformationsList = new ArrayList<>();
        companyList.forEach(company -> {
            List<String> managerList = userManager.getManagerNames(company.getCompanyId()).getBody();
            VisitorCompanyInformations dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorCompanyInformations(company);
            dto.setManagerNames(managerList);
            companyInformationsList.add(dto);
        });
        return companyInformationsList;
    }


    public PersonnelCompanyInformationResponseDto getPersonnelCompanyInformation(String companyId) {
        Company company = findById(companyId).orElseThrow(()->{throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);});
        return ICompanyMapper.INSTANCE.fromCompanyToPersonnelCompanyInformationResponseDto(company);
    }
}
