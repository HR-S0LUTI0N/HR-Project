package com.hrmanagement.service;

import com.hrmanagement.dto.request.AuthCreatePersonnelProfileRequestDto;
import com.hrmanagement.dto.request.ChangeManagerStatusRequestDto;
import com.hrmanagement.dto.request.CreateUserProfileRequestDto;
import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.exception.UserProfileManagerException;
import com.hrmanagement.manager.IAuthManager;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.mapper.IUserProfileMapper;
import com.hrmanagement.rabbitmq.model.PersonnelPasswordModel;
import com.hrmanagement.rabbitmq.producer.PersonelPasswordProducer;
import com.hrmanagement.repository.IUserProfileRepository;
import com.hrmanagement.repository.entity.UserProfile;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IAuthManager authManager;
    private final PersonelPasswordProducer personelPasswordProducer;
    private final ICompanyManager companyManager;
    private final PasswordEncoder passwordEncoder;
    private UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, JwtTokenProvider jwtTokenProvider, PersonelPasswordProducer personelPasswordProducer, ICompanyManager companyManager, PasswordEncoder passwordEncoder){
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
        this.authManager=authManager;
        this.jwtTokenProvider=jwtTokenProvider;
        this.personelPasswordProducer = personelPasswordProducer;
        this.companyManager = companyManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create personnel methoduna
     * Password encoder kurulacak,
     * userId gelmiyor bakılması lzm
     * Manager'le status güncellemesi yapmalıdır.
     * Status active etme diğer tarafta güncellenmelidir.
     * Dto sunda mail mi değil mi validasyonla bakılacak
     * Personnel eklenince STATUS'u null geliyor
     * Şifre validasyonu
     * İlgili kişinin firmasından acacak
     * AdminActiveManager refactor olacak
     * @param token
     * @return
     */
    //çift butonlu olacaktır
    public Boolean adminChangeManagerStatus(String token, ChangeManagerStatusRequestDto dto) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);});
        Optional<UserProfile> optionalAdminProfile = userProfileRepository.findByAuthId(authId);
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        System.out.println(role);
        if(role.contains(ERole.ADMIN.toString())) {
            if (optionalAdminProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            Optional<UserProfile> user = findById(dto.getUserId());
            if (user.get().getRole().contains(ERole.MANAGER)) {
                if (dto.getAction()) {
                    user.get().setStatus(EStatus.ACTIVE);
                } else {
                    user.get().setStatus(EStatus.BANNED);
                }
                update(user.get());
                authManager.updateManagerStatus(IUserProfileMapper.INSTANCE.fromUserProfileToUpdateManagerStatusRequestDto(user.get()));
                return true;
            }
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public Boolean forgotPassword(ForgotPasswordUserResponseDto dto) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(dto.getAuthId());
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        optionalUserProfile.get().setPassword(dto.getPassword());
        update(optionalUserProfile.get());
        return true;
    }

    public Boolean managerCreatePersonelUserProfile(String token, CreateUserProfileRequestDto dto){
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByEmail(dto.getEmail());
        if(optionalUserProfile.isEmpty()) {
            List<String> role = jwtTokenProvider.getRoleFromToken(token);
            System.out.println(role);
            Long managerAuthId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()-> {throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
            Optional<UserProfile> managerProfile = userProfileRepository.findByAuthId(managerAuthId);
            if(managerProfile.isEmpty())
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
            if (role.contains(ERole.MANAGER.toString())) {
                UserProfile userProfile = IUserProfileMapper.INSTANCE.fromCreateUserProfileRequestDtoToUserProfile(dto);
                System.out.println(userProfile);
                userProfile.setPassword(passwordEncoder.encode(dto.getPassword()));
                userProfile.getRole().add(ERole.PERSONEL);
                System.out.println(ERole.PERSONEL);
                userProfile.setStatus(EStatus.ACTIVE);
                userProfile.setCompanyId(managerProfile.get().getCompanyId());
                AuthCreatePersonnelProfileRequestDto authDto = IUserProfileMapper.INSTANCE.fromUserProfileToAuthCreatePersonelProfileRequestDto(userProfile);
                Long personnelAuthId = authManager.managerCreatePersonnelUserProfile(authDto).getBody();
                userProfile.setAuthId(personnelAuthId);
                System.out.println(userProfile);
                save(userProfile);
                PersonnelPasswordModel personnelPasswordModel = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelPasswordModel(userProfile);
                personnelPasswordModel.setPassword(dto.getPassword());
                personelPasswordProducer.sendPersonnelPassword(personnelPasswordModel);
                return true;
            }
            throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
        }
        throw new UserProfileManagerException(ErrorType.USERNAME_DUPLICATE);
    }

    public Boolean createVisitorUser(NewCreateVisitorUserResponseDto dto) {
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateVisitorUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.VISITOR);
        userProfile.setRole(roleList);
        save(userProfile);
        return true;
    }


    public Boolean createManagerUser(NewCreateManagerUserResponseDto dto) {
        System.out.println(dto);
        UserProfile userProfile = IUserProfileMapper.INSTANCE.fromNewCreateManagerUserResponseDtoToUserProfile(dto);
        List<ERole> roleList = new ArrayList<>();
        roleList.add(ERole.MANAGER);
        roleList.add(ERole.PERSONEL);
        userProfile.setStatus(EStatus.PENDING);
        userProfile.setRole(roleList);
        System.out.println(userProfile);
        save(userProfile);
        System.out.println(userProfile);
        return true;
    }


    public String getCompanyId(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return optionalUserProfile.get().getCompanyId();
    }

    public List<String> getManagerNames(String companyId) {
        List<UserProfile> userProfileList = userProfileRepository.findByCompanyId(companyId);
        if(userProfileList.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        List<String> managerList = new ArrayList<>();
        userProfileList.forEach(userProfile -> managerList.add(userProfile.getName()));
        return managerList;
    }

    public Boolean managerDeletePersonnel(String token,String userId){
        UserProfile personnelprofile = findById(userId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        Long managerId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        UserProfile managerProfile = userProfileRepository.findByAuthId(managerId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> role = jwtTokenProvider.getRoleFromToken(token);
        if(role.contains(ERole.MANAGER.toString()) && managerProfile.getCompanyId().equals(personnelprofile.getCompanyId())){
            if(personnelprofile.getStatus() != EStatus.DELETED) {
                personnelprofile.setStatus(EStatus.DELETED);
                update(personnelprofile);
                authManager.managerDeletePersonnel(IUserProfileMapper.INSTANCE.fromUserProfileToDeletePersonnelFromAuthRequestDto(personnelprofile));
                return true;
            }
            throw new UserProfileManagerException(ErrorType.USER_ALREADY_DELETED);
        }
        if(!managerProfile.getCompanyId().equals(personnelprofile.getCompanyId())){
            throw new UserProfileManagerException(ErrorType.DIFFERENT_COMPANY);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public PersonnelInformationResponseDto showPersonnelInformation(String token){
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        if(roles.contains(ERole.PERSONEL.toString())){
            UserProfile userProfile = userProfileRepository.findByAuthId(authId).orElseThrow(()->{throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);});
            PersonnelInformationResponseDto dto = IUserProfileMapper.INSTANCE.fromUserProfileToPersonnelInformationResponseDto(userProfile);
            System.out.println(dto);
            PersonnelCompanyInformationResponseDto companyDto = companyManager.getPersonnelCompanyInformation(userProfile.getCompanyId()).getBody();
            System.out.println(companyDto);
            return IUserProfileMapper.INSTANCE.fromPersonnelCompanyInformationResponseDtoToPersonnelInformationResponseDto(companyDto,dto);
        }
        throw new UserProfileManagerException(ErrorType.AUTHORIZATION_ERROR);
    }

    public Boolean inactivateUser(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        System.out.println("şurdayım");
        System.out.println(optionalUserProfile);
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        System.out.println("şurdayım");
        optionalUserProfile.get().setStatus(EStatus.INACTIVE);
        System.out.println("şurdayım");
        update(optionalUserProfile.get());
        System.out.println(optionalUserProfile);
        return true;
    }

    public UserProfileCommentResponseDto getUserProfileCommentInformation(Long authId) {
        System.out.println(authId);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        System.out.println(optionalUserProfile.get());
        if(optionalUserProfile.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return IUserProfileMapper.INSTANCE.fromUserProfileToUserProfileCommentResponseDto(optionalUserProfile.get());
    }

}



