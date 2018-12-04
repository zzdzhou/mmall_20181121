package jack.project.mmall.dao;

import jack.project.mmall.entity.User;
import org.springframework.data.repository.Repository;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-02
 */
public interface UserRepo extends Repository<User, Integer> {

    int countByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User save(User user);


}
