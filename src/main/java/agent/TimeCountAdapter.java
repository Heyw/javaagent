package agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;


public class TimeCountAdapter extends ClassVisitor implements Opcodes{
    

	private String addFieldName="UMADMS";
	private int acc=ACC_PUBLIC+ACC_FINAL+ACC_STATIC;
	private boolean isPresent;
	private boolean isInterface;
	private String methodName;
	private String className;
	
	public TimeCountAdapter(ClassVisitor classVisitor) {
		super(ASM7, classVisitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		className=name;
		isInterface=(access&ACC_INTERFACE)!=0;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		if(addFieldName.equalsIgnoreCase(name)) {
			
			isPresent=true;
		}
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
	    if(!isInterface&&mv!=null&&!name.equalsIgnoreCase("<init>")&&!name.equalsIgnoreCase("<clinit>")) {
	    	
	       methodName=name;
	       TimeMarkAdapter tma=new TimeMarkAdapter(mv);
	       tma.analyzerAdapter=new AnalyzerAdapter(className, access, name, descriptor, tma);
	       tma.localVarSorter=new LocalVariablesSorter(access, descriptor,tma.analyzerAdapter);
	       return tma.localVarSorter;
	    }
		return mv;
	}
	
	@Override
	public void visitEnd() {
	    if(!isInterface) {
	    	
	    	FieldVisitor fv = cv.visitField(acc, addFieldName, "Ljava/lang/String;", null, className);
	    	if(fv!=null) {
	    		fv.visitEnd();
	    	}
	    }
	    cv.visitEnd();
	}
	class TimeMarkAdapter extends MethodVisitor{

		private AnalyzerAdapter analyzerAdapter;
		private LocalVariablesSorter localVarSorter;
		private int time;
		private int maxStack;
		public TimeMarkAdapter( MethodVisitor methodVisitor) {
			super(ASM7, methodVisitor);
		}
		@Override
		public void visitCode() {
			mv.visitCode();
		    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J",false);
            time=localVarSorter.newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, time);
            maxStack=4;
	}
		
		@Override
		public void visitInsn(int opcode) {
			if(opcode>=IRETURN&&opcode<=RETURN || opcode==ATHROW) {
			    
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
				mv.visitVarInsn(LLOAD, time);
				mv.visitInsn(LSUB);
				
				mv.visitVarInsn(LSTORE, time);
				
				mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
				mv.visitTypeInsn(NEW,"java/lang/StringBuilder");
				mv.visitInsn(DUP);
				mv.visitLdcInsn(" "+methodName+"运行时间:");
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
				mv.visitVarInsn(LLOAD, time);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			
		        maxStack=Math.max(analyzerAdapter.stack.size()+4,maxStack);
			}
			mv.visitInsn(opcode);
		}
		@Override
		public void visitMaxs(int maxStack, int maxLocals) {
			super.visitMaxs(Math.max(maxStack, this.maxStack), maxLocals);
		}
	}
}
