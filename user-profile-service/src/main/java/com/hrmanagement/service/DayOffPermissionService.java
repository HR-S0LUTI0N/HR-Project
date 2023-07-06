package com.hrmanagement.service;

import com.hrmanagement.dto.request.ActionDayOffPermissionDto;
import com.hrmanagement.dto.request.TakeDayOffPermissionRequestDto;
import com.hrmanagement.dto.response.FindAllPendingDayOfPermissionResponseDto;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.mapper.IUserProfileMapper;
import com.hrmanagement.repository.IDayOffPermissionRepository;
import com.hrmanagement.repository.entity.DayOffPermission;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DayOffPermissionService extends ServiceManager<DayOffPermission, String> {
    private final IDayOffPermissionRepository dayOffPermissionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserProfileService userProfileService;

    public DayOffPermissionService(IDayOffPermissionRepository dayOffPermissionRepository, JwtTokenProvider jwtTokenProvider, UserProfileService userProfileService) {
        super(dayOffPermissionRepository);
        this.dayOffPermissionRepository = dayOffPermissionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileService = userProfileService;
    }

    public DayOffPermission takeDayOffPermission(String token, TakeDayOffPermissionRequestDto dto) throws ParseException {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        UserProfile userProfile = userProfileService.findByAuthId(authId.get());
        if (authId.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        if (userProfile.getRole().contains(ERole.PERSONEL)) {
            DayOffPermission dayOffPermission = IUserProfileMapper.INSTANCE.fromTakeDayOffPermissionRequestDtoToDayOffPermission(dto);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startPermissionDate = dateFormat.parse(dayOffPermission.getStartingDate());
            Date endPermissionDate = dateFormat.parse(dayOffPermission.getEndingDate());
            long differentOfTime = endPermissionDate.getTime() - startPermissionDate.getTime();
            long dayOfPermission = (differentOfTime / (1000 * 60 * 60 * 24)) % 365;
            System.out.println("İzin gün sayısı " + dayOfPermission);
            List<DayOffPermission> pendingDayOffPermissionList = dayOffPermissionRepository.findByUserIdAndStatus(userProfile.getUserId(), EStatus.PENDING.toString());
            int pendingDates = 0;
            for (DayOffPermission x : pendingDayOffPermissionList) {
                pendingDates += x.getPermissionDates().size();
            }
            if (userProfile.getEmployeeLeaves() > dayOfPermission + pendingDates) {
                List<String> dateList = getDatesBetween(startPermissionDate, endPermissionDate, dateFormat);
                for (String date : dateList) {
                    System.out.println(date);
                }
                dayOffPermission.setPermissionDates(dateList);
                dayOffPermission.setEPermissionTypes(dto.getEPermissionTypes());
                dayOffPermission.setUserId(userProfile.getUserId());
                dayOffPermission.setStatus(EStatus.PENDING);
                dayOffPermission.setCompanyId(userProfile.getCompanyId());
                save(dayOffPermission);
                return dayOffPermission;
            } else {
                throw new UserProfileManagerException(ErrorType.CANNOT_REQUEST_MORE_LEAVE);
            }
        } else {
            throw new UserProfileManagerException(ErrorType.NOT_PERSONEL);
        }

    }

    public static List<String> getDatesBetween(Date startDate, Date endDate, SimpleDateFormat dateFormat) {
        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            dateList.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return dateList;
    }

    public Boolean actionDayOffPermission(String token, ActionDayOffPermissionDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.contains(ERole.MANAGER.toString())) {
            DayOffPermission dayOffPermission = findById(dto.getPermissionId()).orElseThrow(() -> {
                throw new UserProfileManagerException(ErrorType.NOT_FOUND_DAY_OFF_PERMISSION);
            });
            if (dayOffPermission.getStatus() == EStatus.PENDING) {
                if (dto.getAction()) {
                    dayOffPermission.setStatus(EStatus.ACTIVE);
                    int givenPermissionsDate = dayOffPermission.getPermissionDates().size();
                    Optional<UserProfile> userProfile = userProfileService.findById(dayOffPermission.getUserId());
                    int totalEmployeeLeaves = userProfile.get().getEmployeeLeaves();
                    totalEmployeeLeaves -= givenPermissionsDate;
                    userProfile.get().setEmployeeLeaves(totalEmployeeLeaves);
                    userProfileService.update(userProfile.get());
                } else {
                    dayOffPermission.setStatus(EStatus.DELETED);
                }
                update(dayOffPermission);
                return true;
            }
            throw new UserProfileManagerException(ErrorType.PERMISSION_STATUS_NOT_PENDING);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public List<FindAllPendingDayOfPermissionResponseDto> findAllPendingDayOffPermission(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        UserProfile userProfile = userProfileService.findByAuthId(authId.get());
        if (userProfile.getRole().contains(ERole.MANAGER)) {
            List<DayOffPermission> dayOffPermissionList = dayOffPermissionRepository.findAllByStatusAndCompanyId(EStatus.PENDING.toString(), userProfile.getCompanyId());
            List<FindAllPendingDayOfPermissionResponseDto> findAllPendingDayOfPermissionResponseDtos = dayOffPermissionList.stream().map(x -> {
                FindAllPendingDayOfPermissionResponseDto dto = IUserProfileMapper.INSTANCE.fromToFindAllPendingDayOfPermissionResponseDtoToDayOffPermission(x);
                UserProfile userProfile1 = userProfileService.findById(x.getUserId()).orElseThrow(() -> {
                    throw new UserProfileManagerException(ErrorType.PERSONNEL_NOT_FOUND);
                });
                dto.setPermissionId(x.getPermissionId());
                dto.setName(userProfile1.getName());
                dto.setMiddleName(userProfile1.getMiddleName());
                dto.setSurname(userProfile1.getSurname());
                dto.setPermissionStatus(x.getStatus());
                dto.setDayOffPermission(x.getPermissionDates().size());
                if(userProfile1.getAvatar() != null) {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(userProfile1.getAvatar());
                        String decodedPhoto = new String(decodedBytes);
                        dto.setAvatar(decodedPhoto);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
                return dto;
            }).collect(Collectors.toList());
            return findAllPendingDayOfPermissionResponseDtos;
        }
        throw new UserProfileManagerException(ErrorType.NOT_MANAGER);
    }
}
