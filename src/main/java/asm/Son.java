package asm;

public class Son extends Parent {

	public void run() {
		System.out.println("I can run");
	}
	private void sleep() {
		System.out.println("I cannt walk");
	}
	public void walk() {
		System.out.println("I cannt walk");
	}
	
	public static void main(String[] args) {
		
		Parent son=new Son();
//		son.sleep();
//		son.run();
		son.walk();
	}
}
