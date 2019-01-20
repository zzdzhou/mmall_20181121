package jack.project.mmall.dao;

import jack.project.mmall.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-20
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testSave() {
        User user = new User();
        user.setUsername("Test1");
        user.setEmail("test.ts@test.com");
        user.setRole(User.Role.USER);
        user.setQuestion("This is a test for save()");
        user.setAnswer("yes");
        user.setPassword("af903faoiejwakmojfw");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        User save = userRepo.save(user);
        System.out.println("Test log : " + save);
    }

}


