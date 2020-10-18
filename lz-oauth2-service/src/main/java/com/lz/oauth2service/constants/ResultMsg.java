package com.lz.oauth2service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果信息
 *
 * @author Tan
 * @version 1.1, 2020/10/18 20:46
 */
@Getter
@AllArgsConstructor
public enum ResultMsg {

    /**
     * 公共状态码
     */
    SUCCESS(1, "操作成功"),
    FAIL(0, "操作失败"),
    ERROR(-1, "系统繁忙"),

    ;

    private final int code;
    private final String msg;
}
