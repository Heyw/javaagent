package asm;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cglib.test.EnhancerClient;


public class ClassPrinter extends ClassVisitor {

	public ClassPrinter() {
		super(Opcodes.ASM7);

	}

	/**
	 * 访问class头
	 */
	public void visit(final int version, final int access, final String name, final String signature,
			final String superName, final String[] interfaces) {
		System.out.println(name + " extend " + superName + " implements " + interfaces);
	}

	public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature,
			final Object value) {

		System.out.println(" " + descriptor + " " + name);
		return null;
	}

	public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		System.out.println(" " + name + descriptor);
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		ClassPrinter classPrinter=new ClassPrinter();
		
		//获取当前src/main/java路径下中的EnhancerClient的名称
		InputStream is = ClassLoader.getSystemResourceAsStream(EnhancerClient.class.getName().replace(".", "/")+".class");
	   ClassReader classReader=new ClassReader(is);	
	   classReader.accept(classPrinter, 0);
	}
}
