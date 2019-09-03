package enums;

public enum ResultType {

    OK_SEND             (0, "发送成功"),

    ERROR_COMMON        (100, "错误"),

    ERROR_SEND          (101, "发送异常"),


    ERROR_RECEIVE       (201, "接受异常")

    ;


    ResultType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
