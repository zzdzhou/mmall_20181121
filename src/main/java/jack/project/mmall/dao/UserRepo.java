package jack.project.mmall.dao;

import jack.project.mmall.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-02
 */
public interface UserRepo extends Repository<User, Integer> {

    Optional<User> getById(int id);

    Optional<User> getByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    User save(User user);

    Optional<User> getByEmail(String email);

    @Query(value = "select u.question from User u where u.username = :username")
    String getQuestionByUsername(@Param("username") String username);

//    Optional<User> getUserByUsernameAndQuestionAndAnswer(String username, String question, String answer);

    Optional<User> getByIdAndPassword(int id, String password);



}
