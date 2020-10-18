package com.lz.oauth2service.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lz.oauth2service.component.BaseOAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Tan
 * @version 1.1, 2020/10/18 20:46
 */
@JsonSerialize(using = BaseOAuth2ExceptionSerializer.class)
public class BaseServerErrorException extends BaseAuth2Exception {

	public BaseServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}
