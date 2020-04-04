package asm;

import java.beans.JavaBean;

import jdk.jfr.Description;

@JavaBean
public class AnnotationClass<T> {

	@Description(value="testAnnotation")
	private T value;
}
