package asm;

import java.io.PrintWriter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;
import static org.objectweb.asm.Opcodes.*;


/**
 * CheckClassAdaptor用来校验方法调用和参数是否正确
 * @author Administrator
 *
 */
public class CheckClassAdaptorTest{

	public static void main(String[] args) {
		ClassWriter cw = new ClassWriter(3);
		CheckClassAdapter cca = new CheckClassAdapter(cw);
		TraceClassVisitor tcv = new TraceClassVisitor(cca, new PrintWriter(System.out));
		
		tcv.visit(V10, ACC_PUBLIC+ACC_ABSTRACT+ACC_INTERFACE, "org/by/TestAdaptor", null, "java/lang/Object", null);
		tcv.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "LESS", "I", null, new Integer(-1)).visitEnd();
		tcv.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL, "EQUAL", "I", null, 0).visitEnd();
		tcv.visitField(ACC_PUBLIC+ACC_FINAL+ACC_STATIC,"MORE", "I", null, 1).visitEnd();
		tcv.visitMethod(ACC_PUBLIC+ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
		
		tcv.visitEnd();
		
	}
}
