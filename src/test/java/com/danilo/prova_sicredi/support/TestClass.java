package com.danilo.prova_sicredi.support;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to add an user-friendly test class name to the
 * report.
 * 
 * @author danilo
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface TestClass {
	String value() default "No Name";
}
