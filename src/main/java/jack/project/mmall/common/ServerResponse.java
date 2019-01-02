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

    private ServerResponse(int status) {
        this.code = status;
    }

    private ServerResponse(int status, T data) {
        this.code = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.code = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    /*public int getStatus() {
        return this.code;
    }*/

    /*public String getMsg() {
        return this.msg;
    }*/

    public T getData() {
        return this.data;
    }

    // ---------------------- success response ---------------------------------------------

    public static ServerResponse createBySuccess() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static ServerResponse createBySuccessMsg(String msg) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    // success code 只有一种
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    // ---------------------- error response ---------------------------------------------

    /*public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }*/

    public static <T> ServerResponse<T> createByErrorMsg(String msg) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), msg);
    }

    // error code 有很多
    public static <T> ServerResponse<T> createByError(int errorCode, String errorMsg) {
        return new ServerResponse<T>(errorCode, errorMsg);
    }

}
