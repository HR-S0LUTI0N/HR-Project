package com.hrmanagement.service;

import com.hrmanagement.dto.request.FindPendingCommentWithCompanyName;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.dto.request.SaveCompanyRequestDto;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.IUserManager;
import com.hrmanagement.mapper.ICompanyMapper;
import com.hrmanagement.repository.ICompanyRepository;
import com.hrmanagement.repository.entity.Comment;
import com.hrmanagement.repository.entity.Company;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyService extends ServiceManager<Company, String> {
    private final ICompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final CommentService commentService;

    private CompanyService(ICompanyRepository companyRepository,
                           JwtTokenProvider jwtTokenProvider,
                           IUserManager userManager,
                           CommentService commentService) {
        super(companyRepository);
        this.companyRepository = companyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.commentService = commentService;
    }

    public Boolean save(String token, SaveCompanyRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        if (roles.contains(ERole.ADMIN.toString())) {
            if (!companyRepository.existsByCompanyNameIgnoreCase(dto.getCompanyName())) {
                Company company = ICompanyMapper.INSTANCE.fromSaveCompanyResponseDtoToCompany(dto);
                if(dto.getBase64Logo()!=null){
                    String encodedLogo = Base64.getEncoder().encodeToString(dto.getBase64Logo().getBytes());
                    company.setLogo(encodedLogo);
                }
                save(company);
                return true;
            }
            throw new CompanyManagerException(ErrorType.COMPANY_ALREADY_EXIST);
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    //İlgili müdür için hazırlanan şirket bilgileri getir metodudur. Role'le kontrol ypaılacak
    public CompanyInformationResponseDto showCompanyInformation(String token) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new CompanyManagerException(ErrorType.BAD_REQUEST);
        String companyId = userManager.getCompanyId(authId.get()).getBody();
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        return ICompanyMapper.INSTANCE.fromCompanyToCompanyInformationResponseDto(company);
    }

    //Tüm companylerin preview bilgileri için metot
    public List<VisitorCompanyInformations> findAllCompanyPreviewInformation() {
        List<Company> companyList = companyRepository.findAll();
        List<VisitorCompanyInformations> companyInformationsList = new ArrayList<>();
        companyList.forEach(company -> {
            VisitorCompanyInformations dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorCompanyInformations(company);
            if(company.getLogo()!=null){
                try{
                    byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                    String decodedLogo = new String(decodedBytes);
                    dto.setLogo(decodedLogo);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            companyInformationsList.add(dto);
        });
        return companyInformationsList;
    }

    //Detaylı company sayfası için metot
    public VisitorDetailedCompanyInformationResponse findCompanyDetailedInformation(String companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        VisitorDetailedCompanyInformationResponse dto = ICompanyMapper.INSTANCE.fromCompanyToVisitorDetailedCompanyInformationResponse(company);
        if(company.getLogo()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                String decodedLogo = new String(decodedBytes);
                dto.setLogo(decodedLogo);
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        List<FindCompanyCommentsResponseDto> dtoCommentList = commentService.findCompanyComments(companyId);
        dto.setCompanyComments(dtoCommentList);
        return dto;
    }


    public PersonnelCompanyInformationResponseDto getPersonnelCompanyInformation(String companyId) {
        Company company = findById(companyId).orElseThrow(() -> {
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        });
        PersonnelCompanyInformationResponseDto dto = ICompanyMapper.INSTANCE.fromCompanyToPersonnelCompanyInformationResponseDto(company);
        if(company.getLogo()!=null){
            try{
                byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                String decodedLogo = new String(decodedBytes);
                dto.setLogo(decodedLogo);
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dto;
    }

    public Boolean doesCompanyIdExist(String companyId) {
        if (companyRepository.existsByCompanyId(companyId)) {
            return true;
        }
        return false;
    }

    public List<FindPendingCommentWithCompanyName> findCommentWithCompanyNameByStatus(String token) {
        List<String> userRoles = jwtTokenProvider.getRoleFromToken(token);
        if (userRoles.contains(ERole.ADMIN.toString())) {
            List<Comment> commentList = commentService.findByCommentByStatus();
            List<FindPendingCommentWithCompanyName> pendingComment = commentList.stream()
                    .map(comment -> {
                        Company company = findById(comment.getCompanyId())
                                .orElseThrow(() -> new RuntimeException("Şirket bulunamadı"));
                        String userAvatar = userManager.getUserAvatarByUserId(comment.getUserId()).getBody();
                        FindPendingCommentWithCompanyName pending = FindPendingCommentWithCompanyName.builder()
                                .commentId(comment.getCommentId())
                                .avatar(userAvatar)
                                .companyName(company.getCompanyName())
                                .eCommentStatus(comment.getECommentStatus())
                                .comment(comment.getComment())
                                .name(comment.getName())
                                .surname(comment.getSurname())
                                .build();
                        return pending;
                    })
                    .collect(Collectors.toList());
            return pendingComment;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public String getCompanyNameWithCompanyId(String companyId) {
        Optional<Company> optionalCompany = findById(companyId);
        if (optionalCompany.isEmpty())
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        return optionalCompany.get().getCompanyName();
    }

    public PersonnelDashboardResponseDto getPersonnelDashboardInformation(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.PERSONEL.toString())){
            UserProfilePersonnelDashboardResponseDto userDto = userManager.getUserProfilePersonnelDashboardInformation(authId).getBody();
            System.out.println(userDto);
            PersonnelDashboardResponseDto personnelDto = ICompanyMapper.INSTANCE.fromUserProfilePersonnelDashboardResponseDtoToPersonnelDashboardResponseDto(userDto);
            System.out.println(personnelDto);
            Company company = findById(userDto.getCompanyId()).orElseThrow(()->{throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);});
            personnelDto.setCompanyName(company.getCompanyName());
            System.out.println(1);
            if(company.getLogo()!=null){
                try{
                    byte[] decodedBytes = Base64.getDecoder().decode(company.getLogo());
                    String decodedLogo = new String(decodedBytes);
                    personnelDto.setLogo(decodedLogo);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println(2);
            personnelDto.setSector(company.getSector());
            personnelDto.setHolidayDates(company.getHolidayDates());
            /*
            //TODO Günler ön taraftan date şeklinde kaydedilip long'a çevrildiğinde yapılacak bu şekilde
            for(Long longToDay:company.getHolidayDates()){
                Date newDate = new Date(longToDay);
                dateHoliday.add(newDate);
            }
            List<Date> holidayDatesList = company.getHolidayDates().stream().map(day -> {
                Date newDate = new Date(day);
                return newDate;
            }).collect(Collectors.toList());
            personnelDto.setHolidayDates(holidayDatesList);

            */
            return personnelDto;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }

    public ManagerDashboardResponseDto getManagerDashboardInformation(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new CompanyManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.MANAGER.toString())){
            UserProfileManagerDashboardResponseDto dtoUser = userManager.getUserProfileManagerDashboard(authId).getBody();
            Company company = findById(dtoUser.getCompanyId()).orElseThrow(()->{throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);});
            ManagerDashboardResponseDto managerDto = ICompanyMapper.INSTANCE.fromCompanyToManagerDashboardResponseDto(company);
            managerDto.setCompanyPersonnelCount(dtoUser.getCompanyPersonnelCount());
            return managerDto;
        }
        throw new CompanyManagerException(ErrorType.NO_AUTHORIZATION);
    }
    public AllCompanyInfosForUserProfileResponseDto getAllInfosCompanyWithCompanyId(String companyId) {
        Optional<Company> companyInfos = findById(companyId);
        if (companyInfos.isEmpty())
            throw new CompanyManagerException(ErrorType.COMPANY_NOT_FOUND);
        AllCompanyInfosForUserProfileResponseDto dto=ICompanyMapper.INSTANCE.fromCompanyToAllCompanyInfosForUserProfileResponseDto(companyInfos.get());
        return dto;
    }


}
