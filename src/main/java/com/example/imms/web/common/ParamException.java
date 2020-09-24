package com.example.imms.web.common;

public class ParamException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -5962296753554846774L;

    /**
     * 错误状态码
     */
    private int errorCode;

    public ParamException() {
    }
    /**
     * 错误消息
     * @param msg
     */
    public ParamException(String msg) {
        super(msg);
    }
    public ParamException(int errorCode,String msg){
        super(msg);
        this.errorCode=errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
