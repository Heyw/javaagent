package asm;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.util.TraceClassVisitor;


public class AnnotationAdaptor extends ClassVisitor implements Opcodes {

	public AnnotationAdaptor(ClassVisitor classVisitor) {
		super(ASM7,classVisitor);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
	 FieldVisitor fv = super.visitField(access, name, descriptor, signature, value);
	 if(fv!=null) {
		 
		 fv=new RemoveFieldAnnotation(fv);
	 }
	 return fv;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		if(descriptor.equals("Lasm/AnnotationClass;")) {
			
			return null;
		}
		return new AnnotationValueVisitor(super.visitAnnotation(descriptor, visible));
	}

	public static void main(String[] args) throws IOException {
          ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
          TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
          AnnotationAdaptor aa = new AnnotationAdaptor(tcv);
          
          ClassReader cr = new ClassReader(AnnotationClass.class.getName());
          cr.accept(aa, ClassReader.EXPAND_FRAMES);
	}
	class RemoveFieldAnnotation extends FieldVisitor{

		public RemoveFieldAnnotation( FieldVisitor fieldVisitor) {
			super(ASM7, fieldVisitor);
		}
		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if(descriptor.equals("Lasm/AnnotationClass;")) {//移除一个注解
            	return null;
            }
			return new AnnotationValueVisitor( super.visitAnnotation(descriptor, visible));//对注解的访问
		}
		
		@Override
		public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor,
				boolean visible) {
			// TODO Auto-generated method stub
			return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
		}
	}
	
	class AnnotationValueVisitor extends AnnotationVisitor{

		public AnnotationValueVisitor(AnnotationVisitor annotationVisitor) {
			super(ASM7, annotationVisitor);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void visit(String name, Object value) {
			System.out.println("注解:"+name+value);
			super.visit(name, value);
		}
		
	  @Override
	public AnnotationVisitor visitAnnotation(String name, String descriptor) {
		// TODO Auto-generated method stub
		return super.visitAnnotation(name, descriptor);
	}
	}
}
