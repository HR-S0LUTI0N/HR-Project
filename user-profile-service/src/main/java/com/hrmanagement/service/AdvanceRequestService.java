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
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserProfileRepository userProfileRepository;
    private final IAdvancePermissionRepository advancePermissionRepository;
    private final ICompanyManager companyManager;

    public AdvanceRequestService(JwtTokenProvider jwtTokenProvider, IUserProfileRepository userProfileRepository, IAdvancePermissionRepository advancePermissionRepository, ICompanyManager companyManager) {
        super(advancePermissionRepository);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileRepository = userProfileRepository;
        this.advancePermissionRepository = advancePermissionRepository;
        this.companyManager = companyManager;
    }

    public Boolean advanceRequest(AdvancedRequestDto dto,String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        });
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
      if (optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        if (roles.contains(ERole.PERSONEL.toString())) {
        if (optionalUserProfile.get().getWage() >= dto.getAdvanceRequest()) {
            AdvancePermission advancePermission=IAdvancePermissionMapper.INSTANCE.fromAdvanceRequestDtoToAdvanceRequest(dto);
            advancePermission.setUserId(optionalUserProfile.get().getUserId());
            save(advancePermission);
            return true;
        } else {
            throw new UserProfileManagerException(ErrorType.ADVANCEREQUEST_BIGGER_THAN_WAGE);
        }}else {
            throw new UserProfileManagerException(ErrorType.NOT_PERSONEL);
        }
    }
}