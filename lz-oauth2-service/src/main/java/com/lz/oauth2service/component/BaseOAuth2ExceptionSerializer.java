package com.lz.oauth2service.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.lz.oauth2service.constants.ResultMsg;
import com.lz.oauth2service.exception.BaseAuth2Exception;
import lombok.SneakyThrows;

/**
 * OAuth2 异常格式化
 *
 * @author Tan
 * @version 1.1, 2020/10/18 20:46
 */
public class BaseOAuth2ExceptionSerializer extends StdSerializer<BaseAuth2Exception> {
	public BaseOAuth2ExceptionSerializer() {
		super(BaseAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(BaseAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", ResultMsg.FAIL.getCode());
		gen.writeObjectField("success", false);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}
