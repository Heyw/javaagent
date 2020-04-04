package asm;

import java.io.IOException;

import org.objectweb.asm.util.ASMifier;

public class TestAsmifer {

	long time;
	public void addTimeCount(){
		
		long time=System.nanoTime();
		time=System.nanoTime()-time;
		System.out.println("方法运行时间:"+time);
	}
	public static void main(String[] args) throws IOException {
		
		ASMifier.main(new String[]{"asm.TestAsmifer"});
	}
}
