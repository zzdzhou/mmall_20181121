package jack.project.mmall.util;

import jack.project.mmall.entity.User;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-20
 */
public class BeanUtilsTest {

    @Test
    public void test() {
        User user = new User();
        user.setUsername("Test2");
        user.setEmail("test.ts@test.com");
        user.setRole(User.Role.USER);
        user.setQuestion("This is a test for save()");
//        user.setAnswer("yes");
//        user.setPassword("afidaf9r2ifaj*");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        User dest = new User();
        dest.setUsername("dest");
        dest.setEmail("dest.ds@dest.com");
        dest.setRole(User.Role.USER);
        dest.setQuestion("dest");
        dest.setAnswer("dest");
        dest.setPassword("dest");
        dest.setCreateTime(LocalDateTime.now());
        dest.setUpdateTime(LocalDateTime.now());

        System.out.println(dest);
        BeanUtils.copyPropertiesExceptNull(user, dest, new String[] {"password", "answer"});
        System.out.println(dest);
    }

}
