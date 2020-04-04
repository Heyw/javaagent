package agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class LogerTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		
		try {
			
			ClassReader cr = new ClassReader(className);
			ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
			TimeCountAdapter tca = new TimeCountAdapter(cw);
			cr.accept(tca, ClassReader.EXPAND_FRAMES);
			
			return cw.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
