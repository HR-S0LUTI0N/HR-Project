package com.hrmanagement.service;

import com.hrmanagement.dto.request.*;
import com.hrmanagement.dto.request.LoginRequestDto;
import com.hrmanagement.dto.request.RegisterManagerRequestDto;
import com.hrmanagement.dto.request.RegisterVisitorRequestDto;

import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.AuthManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.manager.ICompanyManager;
import com.hrmanagement.manager.IUserProfileManager;
import com.hrmanagement.mapper.IAuthMapper;
import com.hrmanagement.rabbitmq.model.ForgotPasswordMailModel;
import com.hrmanagement.rabbitmq.model.ResetPasswordModel;
import com.hrmanagement.rabbitmq.producer.ForgotPasswordProducer;
import com.hrmanagement.rabbitmq.producer.RegisterMailHelloProducer;
import com.hrmanagement.rabbitmq.producer.RegisterMailProducer;
import com.hrmanagement.rabbitmq.producer.ResetPasswordProducer;
import com.hrmanagement.repository.IAuthRepository;
import com.hrmanagement.repository.entity.Auth;
import com.hrmanagement.repository.entity.enums.ERole;
import com.hrmanagement.repository.entity.enums.EStatus;
import com.hrmanagement.utility.JwtTokenProvider;
import com.hrmanagement.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hrmanagement.utility.CodeGenerator.generateCode;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    private final RegisterMailHelloProducer registerMailHelloProducer;

    private final IUserProfileManager userManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ForgotPasswordProducer forgotPasswordProducer;

    private final RegisterMailProducer registerMailProducer;
    private final ResetPasswordProducer resetPasswordProducer;
    private final ICompanyManager companyManager;
    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder, RegisterMailHelloProducer registerMailHelloProducer, IUserProfileManager userManager, JwtTokenProvider jwtTokenProvider, ForgotPasswordProducer forgotPasswordProducer, RegisterMailProducer registerMailProducer, ResetPasswordProducer resetPasswordProducer, ICompanyManager companyManager) {
        super(authRepository);
        this.authRepository=authRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMailHelloProducer = registerMailHelloProducer;
        this.userManager = userManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.forgotPasswordProducer = forgotPasswordProducer;
        this.registerMailProducer = registerMailProducer;
        this.resetPasswordProducer = resetPasswordProducer;
        this.companyManager = companyManager;
    }


    public Boolean registerVisitor(RegisterVisitorRequestDto dto){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());
        if(!optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.DUPLICATE_USER);
        Auth auth = IAuthMapper.INSTANCE.fromVisitorsRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.VISITOR));
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            auth.setStatus(EStatus.ACTIVE);
            save(auth);
            userManager.createVisitorUser(IAuthMapper.INSTANCE.fromAuthNewCreateVisitorUserRequestDto(auth));
            registerMailHelloProducer.sendHello(IAuthMapper.INSTANCE.fromAuthToRegisterMailHelloModel(auth));
            return true;
        }
        throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
    }

    public RegisterResponseDto registerManager(RegisterManagerRequestDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromManagerRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.MANAGER,ERole.PERSONEL,ERole.FOUNDER));
        Boolean isCompanyExists = companyManager.doesCompanyExist(dto.getCompanyId()).getBody();
        if(!isCompanyExists)
            throw new AuthManagerException(ErrorType.COMPANY_NOT_FOUND);
        Boolean isFounderExists = userManager.doesFounderExists(dto.getCompanyId()).getBody();
        System.out.println(isFounderExists);
        if(isFounderExists)
            throw new AuthManagerException(ErrorType.FOUNDER_EXIST_ERROR);
        Boolean isSubscriptionExists = companyManager.doesCompanySubscriptionExist(dto.getCompanyId()).getBody();
        if(!isSubscriptionExists)
            throw new AuthManagerException(ErrorType.COMPANY_SUBSCRIPTION_EXIST);

        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setActivationCode(generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            NewCreateManagerUserRequestDto managerUserDto = IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(dto);
            managerUserDto.setAuthId(auth.getAuthId());
            managerUserDto.setPassword(auth.getPassword());
            companyManager.subscribeCompany(IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToSubscribeCompanyRequestDto(dto));
            userManager.createManagerUser(managerUserDto);
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.fromAuthToRegisterMailModel(auth));
        }else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToResponseDto(auth);
        return responseDto;
    }



    public LoginResponseDto login(LoginRequestDto dto){
        Optional<Auth> auth=authRepository.findOptionalByEmail(dto.getEmail());
        if(auth.isEmpty()||!passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())){
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if(!auth.get().getStatus().equals(EStatus.ACTIVE)){
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
        List<String> roleList = auth.get().getRoles().stream().map(x -> x.toString()).collect(Collectors.toList());

        String token = jwtTokenProvider.createToken(auth.get().getAuthId(),roleList)
                .orElseThrow(()->{
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
        return LoginResponseDto.builder().roles(roleList).token(token).build();
    }

    public Boolean forgotPasswordRequest(String email){
        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(email);
        if(optionalAuth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        if(!optionalAuth.get().getStatus().equals(EStatus.ACTIVE))
            throw new RuntimeException("Kullanıcı aktif değil");
        forgotPasswordProducer.sendForgotPassword(ForgotPasswordMailModel.builder()
                        .authId(optionalAuth.get().getAuthId())
                        .email(optionalAuth.get().getEmail())
                .build());
        return true;
    }



    public Boolean confirmUserAccount(String confirmationToken) {
        try {
            Long authId = jwtTokenProvider.getIdFromToken(confirmationToken).orElseThrow(() -> {
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            });
            Optional<Auth> auth = authRepository.findOptionalByAuthId(authId);
            if(auth.get().getStatus()==EStatus.INACTIVE)
                throw new AuthManagerException(ErrorType.INVALID_ACTION);
            auth.get().setStatus(EStatus.INACTIVE);
            update(auth.get());
            userManager.inactivateUser(authId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<Auth> findAll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return authRepository.findAll();
    }

    public Long managerCreatePersonelUserProfile(AuthCreatePersonnelProfileResponseDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromCreatePersonelProfileDtotoAuth(dto);
        auth.setRoles(Arrays.asList(ERole.PERSONEL));
        save(auth);
        return auth.getAuthId();
    }


    public Boolean forgotPassword(String token, ForgotPasswordChangePasswordRequestDto dto) {

            Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            });
            if(!dto.getPassword().equals(dto.getRepassword()))
                throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
            Optional<Auth> optionalAuth = findById(authId);
            optionalAuth.get().setPassword(passwordEncoder.encode(dto.getPassword()));
            update(optionalAuth.get());
            resetPasswordProducer.sendResetPassword(ResetPasswordModel.builder()
                    .email(optionalAuth.get().getEmail())
                    .password(dto.getPassword())
                    .build());
            userManager.forgotPassword(IAuthMapper.INSTANCE.fromAuthToForgotPasswordUserRequestDto(optionalAuth.get()));
            return true;

    }

    public Boolean updateManagerStatus(UpdateManagerStatusResponseDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if(auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        auth.get().setStatus(dto.getStatus());
        update(auth.get());
        return true;
    }

    public Boolean managerDeletePersonnel(DeletePersonnelFromAuthResponseDto dto) {
        Optional<Auth> auth = findById(dto.getAuthId());
        if(auth.isEmpty())
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        auth.get().setStatus(dto.getStatus());
        update(auth.get());
        return true;
    }

    public List<String> getRolesFromToken(String token){
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if(roles.isEmpty())
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        return roles;
    }

    public Boolean updateBecauseOfUserProfile(PersonelUpdateUserProfileToAuthRequestDto dto){
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        IAuthMapper.INSTANCE.updateBecauseOfUserProfile(dto,auth.get());
        update(auth.get());
        return true;
    }
    public Boolean passwordChange(ToAuthPasswordChangeDto dto){
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()){
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        update(auth.get());
        return true;
    }
}
