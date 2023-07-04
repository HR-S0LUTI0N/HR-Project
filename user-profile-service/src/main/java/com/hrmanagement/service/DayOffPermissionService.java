package com.hrmanagement.service;

import com.hrmanagement.dto.request.TakeDayOffPermissionRequestDto;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

            if (userProfile.getEmployeeLeaves() > dayOfPermission) {
                List<String> dateList = getDatesBetween(startPermissionDate, endPermissionDate, dateFormat);
                for (String date : dateList) {
                    System.out.println(date);
                }
                dayOffPermission.setPermissionDates(dateList);
                dayOffPermission.setEPermissionTypes(dto.getEPermissionTypes());
                dayOffPermission.setUserId(userProfile.getUserId());
                dayOffPermission.setStatus(EStatus.PENDING);
                save(dayOffPermission);
                return dayOffPermission;
            } else {
                throw new RuntimeException("Mevcut izin gün sayınızdan fazla izin talep edemezsiniz");
            }
        } else {
            throw new RuntimeException("ROlü personel değil");
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
}
