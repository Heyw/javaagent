package asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class AddField extends ClassVisitor {

	private String addFieldName = "addField";
	private boolean isPresient = false;

	public AddField(ClassVisitor classVisitor) {
		super(Opcodes.ASM7, classVisitor);
	}


	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {

		if (name.equalsIgnoreCase(addFieldName)) {

			isPresient = true;
		}
		return super.visitField(access, name, descriptor, signature, value);
	}

	@Override
	public void visitEnd() {

		if (!isPresient) {

			FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, addFieldName, "I", null, 3);
			if(fv!=null) {
				fv.visitEnd();
			}
		}
		super.visitEnd();
	}

	public static void main(String[] args) throws IOException {

		ClassWriter cw = new ClassWriter(3);// 用来生成class文件
		AddField addField = new AddField(cw);// ClassVisitor装饰器，按照ClassReader的解析方式生成对象

		ClassReader cr = new ClassReader(Student.class.getName());
    	cr.accept(addField, 0);//0代表所有都会解析
		
		Path filePath = Paths.get("org/by/Cwtest1.class");
		File file = filePath.toFile();
		File parent = filePath.getParent().toFile();
		parent.mkdirs();//创建多级目录
		file.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(cw.toByteArray());
		fos.close();
	}

}
