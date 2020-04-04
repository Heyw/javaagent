package cglib.test;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class EnhancerClient {
     int a=1;
	public static void main(String[] args) {
		
		//代理类class文件存入本地磁盘方便我们反编译查看源码
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D://code");
		//通过CGLIB动态代理获取代理对象的过程
		Enhancer enhancer=new Enhancer();
		//设置enhancer对象的父类
		enhancer.setSuperclass(HelloService.class);
		//设置enhancer的回调对象
		enhancer.setCallback(new MyMethodInterceptor());
		//创建代理对象
		HelloService proxy=(HelloService)enhancer.create(new Class[] {int.class}, new Object[]{1});
		//通过代理对象调用目标方法
		proxy.sayHello();
	}
}
