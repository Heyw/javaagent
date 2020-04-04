package asm;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.objectweb.asm.util.TraceClassVisitor;


public class GenericAdaptor extends ClassVisitor implements Opcodes {

	public GenericAdaptor(ClassVisitor cv) {
		super(ASM7, cv);
	}
	
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
    		String[] exceptions) {
        if(signature!=null) {
        	
        	signature=addGenericM(signature);
        }
    	return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    	if(signature!=null) {
    		signature=addGenericM(signature);
    	}
    	super.visit(version, access, name, signature, superName, interfaces);
    }
    private String addGenericM(String signature) {
    	SignatureWriter sw = new SignatureWriter();
    	GenericAdder genericAdder = new GenericAdder(sw);
    	SignatureReader sr = new SignatureReader(signature);
    	sr.accept(genericAdder);
    	return sw.toString();
    }
    
    public static void main(String[] args) throws IOException {
		ClassReader cr= new ClassReader(Node.class.getName());
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		TraceClassVisitor tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
		GenericAdaptor ga = new GenericAdaptor(tcv);
		
		cr.accept(ga, ClassReader.EXPAND_FRAMES);
	}
    class GenericAdder extends SignatureVisitor{

    	private SignatureVisitor sv;
		public GenericAdder(SignatureVisitor sv) {
			super(ASM7);
			this.sv=sv;
		}
    	
		@Override
		public void visitClassType(String name) {
			
			this.sv.visitClassType(name+" M:Ljava/lang/Object;");
		}
    }
}
