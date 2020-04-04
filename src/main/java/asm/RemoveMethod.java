package asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RemoveMethod extends ClassVisitor{

	public RemoveMethod( ClassVisitor cv) {
		super(Opcodes.ASM7, cv);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// TODO Auto-generated method stub
		super.visit(version, access, "org/by/CWTest", signature, superName, interfaces);
	}
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
    		String[] exceptions) {
    	if(name.startsWith("do")) {
    		return null;
    	}
    	return cv.visitMethod(access, name, descriptor, signature, exceptions);
    }
    
    public static void main(String[] args) throws IOException {
		
    	ClassWriter cv=new ClassWriter(3);
    	RemoveMethod rm=new RemoveMethod(cv);
    	ClassReader cr=new ClassReader(Student.class.getName());
    	cr.accept(rm, 0);
    	
//    	File file=new File("org/by/CWTest.class");
    	Path filePath = Paths.get("org/by/CWTest.class");
    	File parentFile= filePath.getParent().toFile();
    	
    	parentFile.mkdirs();
    	File file = filePath.toFile();
    	file.createNewFile();
    	FileOutputStream fos=new FileOutputStream(file);
    	fos.write(cv.toByteArray());
    	fos.close();
    	
	}
}
