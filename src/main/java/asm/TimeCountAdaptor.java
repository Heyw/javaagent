package asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.util.TraceClassVisitor;

public class TimeCountAdaptor extends ClassVisitor implements Opcodes {

	private String owner;
	private Boolean isInterface;

	public TimeCountAdaptor(ClassVisitor cv) {
		super(ASM7, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		owner=name;
		isInterface=(access&ACC_INTERFACE)!=0;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
		if(!isInterface&&mv!=null&&!name.equals("<init>")) {
			AddTimerMethodAdaptor atmAdap = new AddTimerMethodAdaptor(mv);
			atmAdap.aa=new AnalyzerAdapter(owner, access, name, descriptor, atmAdap);
			atmAdap.lvs=new LocalVariablesSorter(access, descriptor, atmAdap.aa);
			return atmAdap.lvs;
		}
		return mv;
	}
	
	@Override
	public void visitEnd() {
		cv.visitEnd();
	}

	class AddTimerMethodAdaptor extends MethodVisitor {

		private int time;
		private int maxStack;
		public LocalVariablesSorter lvs;
		public AnalyzerAdapter aa;

		public AddTimerMethodAdaptor(MethodVisitor mv) {
			super(ASM7, mv);
		}

		@Override
		public void visitCode() {
			mv.visitCode();
			// System.nanoTime
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
			time = lvs.newLocal(Type.LONG_TYPE);// 创建本地变量
			mv.visitVarInsn(LSTORE, time);// 保存本地变量
			maxStack = 4;
		}

		@Override
		public void visitInsn(int opcode) {
			
			if((opcode>=IRETURN&&opcode<=RETURN)|| opcode==ATHROW) {
				
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
				mv.visitVarInsn(LLOAD, time);
				mv.visitInsn(LSUB);
			    mv.visitVarInsn(LSTORE, time);
			    
			    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			    mv.visitVarInsn(LLOAD, time);
			    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
			    maxStack=Math.max(aa.stack.size()+4, maxStack);
			}
			mv.visitInsn(opcode);
		}

		@Override
		public void visitMaxs(int maxStack, int maxLocals) {
			// TODO Auto-generated method stub
			super.visitMaxs(Math.max(maxStack, this.maxStack), maxLocals);
		}
	}
	public static void main(String[] args) throws IOException {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		TraceClassVisitor tcv=new TraceClassVisitor(cw,new PrintWriter(System.out));
		TimeCountAdaptor tca=new TimeCountAdaptor(tcv);
		
		ClassReader cr = new ClassReader("asm.Receiver");
		cr.accept(tca, ClassReader.EXPAND_FRAMES);
		
		Path filePath = Paths.get("target/classes/ams/Receiver.class");
		File file = filePath.toFile();
		File parent = filePath.getParent().toFile();
		if(!parent.isDirectory()) {
			parent.mkdirs();
		}
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(cw.toByteArray());
		fos.close();
	}


}
