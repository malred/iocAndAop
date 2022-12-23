package org.malred.edu.factory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工厂类,用反射实例化对象
 */
public class BeanFactory {
    /**
     * 1,解析xml,通过反射实例化对象,存入一个map
     * 2,对外提供获取bean的接口(根据id获取)
     */
    private static Map<String, Object> map = new HashMap<>();

    // 静态方法,类加载时执行,1解析xml,并实例化对象放入map
    static {
        // 读取xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            // 获取根元素
            Element rootElement = document.getRootElement();
            // 得到所有bean标签
            List<Element> beanList = rootElement.selectNodes("//bean");
            for (int i = 0; i < beanList.size(); i++) {
                Element element = beanList.get(i);
                // 处理每个bean元素,获取该元素的id和class属性
                String id = element.attributeValue("id");//accountDao
                //org.malred.edu.dao.impl.JdbcAccountDaoImpl
                String clazz = element.attributeValue("class");
                // 通过反射实例化对象
                Class<?> aClass = Class.forName(clazz);
                Object o = aClass.newInstance();// 实例化对象
                // 存放到map中
                map.put(id, o);
            }
            // 实例化完成后(维护对象依赖关系),检查哪些对象需要传值,根据它的配置,传入相应的值
            // bean有property子标签的就需要传值
            List<Element> propertyList = rootElement.selectNodes("//property");
            // 解析property,获取父元素
            for (int i = 0; i < propertyList.size(); i++) {
                // <property name="AccountDao" ref="accountDao"></property>
                Element element = propertyList.get(i);
                // 获取name属性
                String name = element.attributeValue("name");
                // 获取ref属性
                String ref = element.attributeValue("ref");
                // 找到当前需要传值的bean(处理依赖关系)
                Element parent = element.getParent();
                // 得到父元素
                String pid = parent.attributeValue("id");
                Object parentObj = map.get(pid);
                // 遍历父元素所有方法,获取set方法
                Method[] methods = parentObj.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    // 忽略大小写地匹配 方法名 == set+name
                    if (method.getName().equalsIgnoreCase("set" + name)) {//setAccountDao
                        // 参数1: 是谁的方法
                        // 参数2[不定长]: 方法(实)参数
                        method.invoke(parentObj, map.get(ref));
                    }
                }
                // 处理后的父元素重新放到map
                map.put(pid, parentObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2,对外提供获取实例对象的接口(根据id获取)
    public static Object getBean(String id) {
        return map.get(id);
    }
}
