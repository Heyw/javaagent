package asm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ClassWriterTest {

	public static void main(String[] args) throws IOException {
		ClassWriter cw = new ClassWriter(0);
		//类版本，访问标志，类全名，泛型，父类，接口
		cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE, ClassWriterTest.class.getPackageName()+"/TestClassWriter",
				null, "java/lang/Object", null);
		//访问标志，名字，类型，泛型，值
		cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL, "Less", "I", null, new Integer(-1)).visitEnd();
		cw.visitField(Opcodes.ACC_PRIVATE+Opcodes.ACC_FINAL,"str","Ljava/lang/String",null,"hello").visitEnd();;
	//访问标志，名字，签名，泛型，throws异常
		cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
		cw.visitEnd();//通知classrWriter，类定义完成
		String systemRootUrl=(new File("")).toURI().toURL().getPath();
		File file=new File(ClassWriterTest.class.getResource("").getPath()+"/TestClassWriter.class");
		String parent=file.getParent();
		File parent1=new File(parent);
		parent1.mkdir();
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(cw.toByteArray());
		fileOutputStream.close();
	}
}
