package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public BaseResponse<T> ok() {
        this.code = 0;
        this.message = "success";
        return this;
    }

    public BaseResponse<T> error() {
        this.code = 1;
        this.message = "error";
        return this;
    }

    public BaseResponse<T> error(String message) {
        this.code = 1;
        this.message = message;
        return this;
    }

    public BaseResponse<T> ok(T data) {
        this.code = 0;
        this.message = "success";
        this.data = data;
        return this;
    }


}
