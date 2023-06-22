package com.hrmanagement.controller;

import com.hrmanagement.dto.request.LoginRequestDto;
import com.hrmanagement.dto.request.PersonelUpdateUserProfileToAuthRequestDto;
import com.hrmanagement.dto.request.RegisterManagerRequestDto;
import com.hrmanagement.dto.request.RegisterVisitorRequestDto;

import com.hrmanagement.dto.response.*;
import com.hrmanagement.exception.AuthManagerException;
import com.hrmanagement.exception.ErrorType;
import com.hrmanagement.repository.entity.Auth;
import com.hrmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.hrmanagement.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(REGISTER_VISITOR)
    public ResponseEntity<Boolean> registerVisitor(@RequestBody @Valid RegisterVisitorRequestDto dto){
        return ResponseEntity.ok(authService.registerVisitor(dto));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(REGISTER_MANAGER)
    public ResponseEntity<RegisterResponseDto> registerManager(@RequestBody @Valid RegisterManagerRequestDto dto){
        return ResponseEntity.ok(authService.registerManager(dto));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(FORGOT_PASSWORD_REQUEST + "/{email}")
    public ResponseEntity<Boolean> forgotPasswordRequest(@PathVariable String email){
        return ResponseEntity.ok(authService.forgotPasswordRequest(email));
    }



    @GetMapping(CONFIRM_ACCOUNT)
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String token) throws URISyntaxException {
        if(authService.confirmUserAccount(token)){
            URI forgotPasswordSuccessful = new URI("http://localhost:3000/confirm-manager");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(forgotPasswordSuccessful);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }else{
            URI forgotPasswordSuccessful = new URI("http://localhost:3000/404");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(forgotPasswordSuccessful);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @Hidden
    @PostMapping("/manager-create-personnel-userProfile")
    public ResponseEntity<Long> managerCreatePersonnelUserProfile(@RequestBody AuthCreatePersonnelProfileResponseDto dto){
        return ResponseEntity.ok(authService.managerCreatePersonelUserProfile(dto));
    }

    @GetMapping(FORGOT_PASSWORD + "/{token}")
    public ResponseEntity<Object> forgotPassword(@PathVariable String token) throws URISyntaxException {
        if(authService.forgotPassword(token)){
            URI forgotPasswordSuccessful = new URI("http://localhost:3000/forgotpassword-notification");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(forgotPasswordSuccessful);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }else{
            URI forgotPasswordSuccessful = new URI("http://localhost:3000/404");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(forgotPasswordSuccessful);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }

    }

    @PutMapping("/update-manager-status")
    public ResponseEntity<Boolean> updateManagerStatus(@RequestBody UpdateManagerStatusResponseDto dto){
        return ResponseEntity.ok(authService.updateManagerStatus(dto));
    }

    @PutMapping("/manager-delete-personnel")
    public ResponseEntity<Boolean> managerDeletePersonnel(@RequestBody DeletePersonnelFromAuthResponseDto dto){
        return ResponseEntity.ok(authService.managerDeletePersonnel(dto));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/get-roles-from-token/{token}")
    public ResponseEntity<List<String>> getRolesFromToken(@PathVariable String token){
        return ResponseEntity.ok(authService.getRolesFromToken(token));
    }
    @Hidden
    @PutMapping("/update-userprofile-to-auth")
    public ResponseEntity<Boolean> updatePersonel(@RequestBody PersonelUpdateUserProfileToAuthRequestDto dto){
        return ResponseEntity.ok(authService.updateBecauseOfUserProfile(dto));
    }

}
