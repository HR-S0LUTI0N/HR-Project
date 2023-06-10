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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hrmanagement.utility.CodeGenerator.generateCode;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    private final IUserProfileManager userManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ForgotPasswordProducer forgotPasswordProducer;

    private final RegisterMailProducer registerMailProducer;
    private final ResetPasswordProducer resetPasswordProducer;
    private final ICompanyManager companyManager;
    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder, IUserProfileManager userManager, JwtTokenProvider jwtTokenProvider, ForgotPasswordProducer forgotPasswordProducer, RegisterMailProducer registerMailProducer, ResetPasswordProducer resetPasswordProducer, ICompanyManager companyManager) {
        super(authRepository);
        this.authRepository=authRepository;
        this.passwordEncoder = passwordEncoder;
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
            return true;
        }
        throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
    }

    public RegisterResponseDto registerManager(RegisterManagerRequestDto dto){
        Auth auth = IAuthMapper.INSTANCE.fromManagerRequestDtoToAuth(dto);
        auth.setRoles(List.of(ERole.MANAGER,ERole.PERSONEL));
        Boolean isCompanyExists = companyManager.doesCompanyExist(dto.getCompanyId()).getBody();
        if(!isCompanyExists)
            throw new AuthManagerException(ErrorType.COMPANY_NOT_FOUND);
        if (dto.getPassword().equals(dto.getRepassword())){
            auth.setActivationCode(generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            NewCreateManagerUserRequestDto managerUserDto = IAuthMapper.INSTANCE.fromRegisterManagerRequestDtoToNewCreateManagerUserRequestDto(dto);
            managerUserDto.setAuthId(auth.getAuthId());
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



    public String confirmUserAccount(String confirmationToken) {

        try{
            if(confirmationToken != null) {
                Long authId = jwtTokenProvider.getIdFromToken(confirmationToken).get();
                Optional<Auth> auth = authRepository.findOptionalByAuthId(authId);
                auth.get().setStatus(EStatus.INACTIVE);
                update(auth.get());
                userManager.inactivateUser(authId);
                return "accountVerified";
            } else
            {
                return "Error:"+" "+"The link is invalid or broken!";
            }
        }catch (Exception e){
            throw new RuntimeException("Beklenmeyen bir hata oluştu.");
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
        save(auth);
        return auth.getAuthId();
    }


    public Boolean forgotPassword(String token) {
        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(() -> {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        });
        Optional<Auth> optionalAuth = findById(authId);
        String newPassword = UUID.randomUUID().toString();
        optionalAuth.get().setPassword(passwordEncoder.encode(newPassword));
        update(optionalAuth.get());
        resetPasswordProducer.sendResetPassword(ResetPasswordModel.builder()
                        .email(optionalAuth.get().getEmail())
                        .password(newPassword)
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
}
