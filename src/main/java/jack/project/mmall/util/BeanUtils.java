package jack.project.mmall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-19
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static void copySpecifedProperties(Object bean, String[] names, Object value) throws InvocationTargetException, IllegalAccessException {
        for (String name : names) {
            org.apache.commons.beanutils.BeanUtils.copyProperty(bean, name, value);
        }
    }

    public static void copyPropertiesExceptNull(Object source, Object dest, String[] ignoredProperties) {
        HashSet<String> strSet = new HashSet<>();
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field item : fields) {
            item.setAccessible(true);
            try {
                if (item.get(source) == null) {
                    strSet.add(item.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Collections.addAll(strSet, ignoredProperties);
        logger.debug(strSet.toString());
        copyProperties(source, dest, strSet.toArray(new String[0]));
    }

}
