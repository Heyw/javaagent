package asm;

public class Node <T>{

	int key;
	T value;
	
	public <K> void do1(K v) {
		
		System.out.println("ss1"+v);
	}
	
	public static void main(String[] args) {
		
		Node n=new Node();
		n.do1(4);
	}
}
