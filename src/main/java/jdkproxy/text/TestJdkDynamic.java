package jdkproxy.text;

import java.lang.reflect.Proxy;

public class TestJdkDynamic {

	public static void main(String[] args) {
		
		HelloService hs=new HelloServiceImpl();
		HelloInvocationHandler handler=new HelloInvocationHandler();
		handler.setSubj(hs);
		
		HelloService helloService = (HelloService)Proxy.newProxyInstance(hs.getClass().getClassLoader(), hs.getClass().getInterfaces(), handler);
//		System.out.println("proxy hashcode:"+helloService.hashCode());
		if(helloService instanceof Proxy) {
			helloService.sayHello();
		}
		
	}
}
