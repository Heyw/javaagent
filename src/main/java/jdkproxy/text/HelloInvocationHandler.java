package jdkproxy.text;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloInvocationHandler implements InvocationHandler {

	private Object subj;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
//		System.out.println("interceptor proxy hashcode:"+proxy.hashCode());
		return method.invoke(subj, args);//在代理实例上激活相应的方法
	}
 public void setSubj(Object subj) {
	 
	 this.subj=subj;
 }
}
