package jack.project.mmall.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Theme:
 * <p>
 * Description:
 * 用来代替 User，在以下情况下 1/ 返回给前端的 User; 2/ session 中的User;
 * 与 User 相比， 删除 password, answer 字段
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private int id;

    private String username;

    private String email;

    private String phone;

    private String question;

    private int role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public UserResponse() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
