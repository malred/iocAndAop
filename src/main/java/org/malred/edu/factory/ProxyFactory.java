package org.malred.edu.factory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.malred.edu.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 工厂类: 统一返回代理类
 */
public class ProxyFactory {
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    // 工厂类来实例化单例
    // 单例
//    private static ProxyFactory instance = new ProxyFactory();
//
//    private ProxyFactory() {
//    }
//
//    public static ProxyFactory getInstance() {
//        return instance;
//    }

    /**
     * jdk动态代理
     *
     * @param obj 传入实现了接口的类
     */
    public Object getJdkProxy(Object obj) {
        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = null;
                        try {
                            // 开启事务(关闭事务的自动提交,jdbc默认自动提交)
//                            TransactionManager.getInstance().beginTransaction();
                            transactionManager.beginTransaction();
                            // 调用原方法逻辑
                            result = method.invoke(obj, args);
                            // 提交事务
//                            TransactionManager.getInstance().commit();
                            transactionManager.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 回滚事务
//                            TransactionManager.getInstance().rollback();
                            transactionManager.rollback();
                            throw e; // 抛出异常,让servlet捕获
                        }
                        return result;
                    }
                });
    }

    /**
     * cglib动态代理
     *
     * @param obj 类(不需要实现接口)
     * @return
     * @throws Exception
     */
    public Object getCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                try {
                    // 开启事务(关闭事务的自动提交,jdbc默认自动提交)
//                    TransactionManager.getInstance().beginTransaction();
                    transactionManager.beginTransaction();
                    // 调用原方法逻辑
                    result = method.invoke(obj, objects);
                    // 提交事务
//                    TransactionManager.getInstance().commit();
                    transactionManager.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 回滚事务
//                    TransactionManager.getInstance().rollback();
                    transactionManager.rollback();
                    throw e; // 抛出异常,让servlet捕获
                }
                return result;
            }
        });
    }
}
