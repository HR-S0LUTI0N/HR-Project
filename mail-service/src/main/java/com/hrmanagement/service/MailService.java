package com.hrmanagement.service;

import com.hrmanagement.rabbitmq.model.*;
import com.hrmanagement.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;

    public void sendMail(RegisterMailModel registerMailModel){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(registerMailModel.getEmail());
        mailMessage.setSubject("KAYDI TAMAMLAYIN LÜTFEN");
        mailMessage.setText(
                registerMailModel.getName()+" " + registerMailModel.getSurname() + " To confirm your account, please click here :\n"  +
                        "http://localhost:9090/api/v1/auth/confirm-account?token="+jwtTokenProvider.createMailToken(registerMailModel.getAuthId(), registerMailModel.getStatus()).get()
        );
        javaMailSender.send(mailMessage);
    }



    public void forgotPasswordRequestMail(ForgotPasswordMailModel model){
        String token = jwtTokenProvider.createTokenForForgotPassword(model.getAuthId()).get();
        String linkForgotPasswordLink = "http://localhost:3000/forgotpassword-replace/";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("Forgot Password");
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setText("Dear User, \n"
                + "If you want to change the your password, please click the link at the below!"
                + "\n" + linkForgotPasswordLink+token);
        javaMailSender.send(mailMessage);
    }

    public void resetPasswordMail(ResetPasswordModel model){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("New Password");
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setText("Dear User,\n " +
        "Your new password is changed successfully! You can login with your new password: " + model.getPassword());
        javaMailSender.send(mailMessage);

    }

    public void personnelPasswordMail(PersonnelPasswordModel model){
        System.out.println(model);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("New Personnel Registration Mail");
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setText("Dear User,\n" +
                "Welcome aboard you can access our website with the information stated at the below.\n"
        +   "email: " + model.getEmail()
        +   "\npassword: " + model.getPassword());
        javaMailSender.send(mailMessage);
    }

    public void sendHelloMail(RegisterMailHelloModel registerMailHelloModel) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(registerMailHelloModel.getEmail());
        mailMessage.setSubject("AKTIVASYON KODU");
        mailMessage.setText(
                "Hello visitor, " +  registerMailHelloModel.getName() +" "+ registerMailHelloModel.getSurname()+" you have successfully registered\n"
                        +   "email: " + registerMailHelloModel.getEmail()

        );
        javaMailSender.send(mailMessage);
    }


    public void sendInfoMailForManager(ManagerChangeStatusModel model) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("MANAGER ACTIVATION INFORMATION");
        if (model.getStatus().equals("ACTIVE"))
            mailMessage.setText("Welcome Dear "+model.getName()+";\n"+ "Your account has been ACTIVATED. Thank you for choosing us");
        else
            mailMessage.setText("Welcome Dear "+model.getName()+";\n" + "Your account has been BANNED.");
        javaMailSender.send(mailMessage);
    }
}
