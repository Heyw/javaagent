package asm;

public class Student {

	private String name;
	public String name() {
		
		return this.name;
	}
	public void setName(String name,String backup) {

		byte b='b';
		
		char[] ca= {'a','b','c','d'};
		char c='c'+'d'+'a'+'e';
		
		int a=new Integer(5);
		
		short s=32767;
		
		char e=ca[3];
		float f=0.1f;
		double d=1.000000002d;
		int i1=40;
		int i2=32768;
		i2++;
		
		long l1=190989000;
		
		this.name=name+b+c;
	}
	
	public void testOp() {
		
		int a=1;
		int b=2;
		int c=a+b;
		int d=a/b;
		int e=a*b;
		int f=a%b;
		int g=a++;
		float h=12.01f;
		c=c/'a';
	}
	public void print() {
		System.out.println(this.name);
	}
}
