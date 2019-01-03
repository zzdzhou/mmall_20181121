package jack.project.mmall.common;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-01
 */
public enum ResponseCode {

    SUCCESS(0, "success"),
    ERROR(1, "error"),
    NEED_LOGIN(10, "need login"),
    ILLEGAL_ARGUMENT(2, "illegal argument");

    private int code;

    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
