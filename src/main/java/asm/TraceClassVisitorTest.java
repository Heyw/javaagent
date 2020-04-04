package asm;

import java.io.PrintWriter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;





public class TraceClassVisitorTest {

	public static void main(String[] args) {
		ClassWriter classWriter = new ClassWriter(3);
		TraceClassVisitor tv = new TraceClassVisitor(classWriter,new PrintWriter(System.out));
		tv.visit(Opcodes.V10, Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE, "org/by/TestInterface", null, "java/lang/Object", null);
	    tv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL, "fieldInt", "I" ,null, new Integer(-1)).visitEnd();
	    tv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"fieldString", "Ljava/lang/String", null, "filedStr").visitEnd();;
	    tv.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "getField", "(ILjava/lang/String;)Ljava/lang/String", null, null).visitEnd();
	    
	    tv.visitEnd();
	}
}
