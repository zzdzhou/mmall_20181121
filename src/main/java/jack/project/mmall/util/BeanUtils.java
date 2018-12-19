package jack.project.mmall.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-19
 */
public class BeanUtils {

    public static void copySpecifedProperties(Object bean, String[] names, Object value) throws InvocationTargetException, IllegalAccessException {
        for (String name : names) {
            org.apache.commons.beanutils.BeanUtils.copyProperty(bean, name, value);
        }
    }
}
