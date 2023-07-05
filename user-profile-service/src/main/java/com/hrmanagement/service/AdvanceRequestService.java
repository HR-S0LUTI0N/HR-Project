package com.hrmanagement.service;

import com.hrmanagement.dto.request.AdvancedRequestDto;
import com.hrmanagement.dto.response.CompanyNameAndWageDateResponseDto;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.mapper.IAdvancePermissionMapper;
import com.hrmanagement.repository.IAdvancePermissionRepository;
import com.hrmanagement.repository.IUserProfileRepository;
import com.hrmanagement.repository.entity.AdvancePermission;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.EAdvanceStatus;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class AdvanceRequestService extends ServiceManager<AdvancePermission, String> {

    private final IAdvancePermissionRepository advancePermissionRepository;


    public AdvanceRequestService(JwtTokenProvider jwtTokenProvider, IUserProfileRepository userProfileRepository, IAdvancePermissionRepository advancePermissionRepository, ICompanyManager companyManager) {
        super(advancePermissionRepository);
        this.advancePermissionRepository = advancePermissionRepository;
    }

    public List<AdvancePermission> findAllByStatusAndCompanyId(EAdvanceStatus status, String companyId){
        List<AdvancePermission> advancePermissionList = advancePermissionRepository.findAllByStatusAndCompanyId(status,companyId);
        return advancePermissionList;
    }







}