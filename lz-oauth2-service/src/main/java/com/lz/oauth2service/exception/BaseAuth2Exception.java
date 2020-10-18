package com.lz.oauth2service.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lz.oauth2service.component.BaseOAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 *
 * @author Tan
 * @version 1.1, 2020/10/18 20:46
 */
@JsonSerialize(using = BaseOAuth2ExceptionSerializer.class)
public class BaseAuth2Exception extends OAuth2Exception {
    @Getter
    private String errorCode;

    public BaseAuth2Exception(String msg) {
        super(msg);
    }

    public BaseAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
