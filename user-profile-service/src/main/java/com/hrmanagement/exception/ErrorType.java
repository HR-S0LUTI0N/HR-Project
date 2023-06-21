package com.hrmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(5100, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4000, "Parametre Hatası", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4300, "Bu kullanıcı zaten kayıtlı", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_CREATED(4100, "Kullanıcı oluşturulamadı", HttpStatus.BAD_REQUEST),
    AUTHORIZATION_ERROR(4200,"You're not authorized to do this.", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(4400, "Böyle bir kullanıcı bulunamadı", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4600,"Token hatası" ,  HttpStatus.BAD_REQUEST),
    FOLLOW_ALREADY_EXIST(4700, "Böyle bir takip isteği daha önce oluşturulmuştur.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOLLOW(4800, "Kullanıcı kendisini takip edemez.", HttpStatus.BAD_REQUEST),
    PASSWORD_ERROR(4900, "Girdiğiniz şifre ile eski şifreniz uyuşmamaktadır.", HttpStatus.BAD_REQUEST),
    INVALID_ACTION(5000,"Kullanıcı istenilen statüye geçirilemedi.",HttpStatus.BAD_REQUEST),
    USER_ALREADY_DELETED(5200,"Kullanıcı zaten silinmiştir.",HttpStatus.BAD_REQUEST),
    DIFFERENT_COMPANY(5300,"Personel başka bir şirkette çalışmaktadır." , HttpStatus.BAD_REQUEST),
    NOT_PERSONEL(5400,"Bu işlemi yapabilmek için personel olmanız gerekmektedir.",HttpStatus.NOT_FOUND);
    private int code;
    private String message;
    HttpStatus httpStatus;
}
