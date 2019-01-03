package jack.project.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-11-29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    private ServerResponse(int code) {
        this.code = code;
    }

    private ServerResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    // ---------------------- success response ---------------------------------------------

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMsg(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    // success code 只有一种
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    // ---------------------- error response ---------------------------------------------

    /*public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }*/

    public static <T> ServerResponse<T> createByErrorMsg(String msg) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), msg);
    }

    // error code 有很多
    public static <T> ServerResponse<T> createByError(int errorCode, String errorMsg) {
        return new ServerResponse<>(errorCode, errorMsg);
    }

    // ---------------------- response code ---------------------------------------------

    public static <T> ServerResponse<T> createByResponseCode(ResponseCode code) {
        return new ServerResponse<>(code.getCode(), code.getDescription());
    }

}
