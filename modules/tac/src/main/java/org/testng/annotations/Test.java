package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to trick TestNG that this is a test to run.
 *
 * @since 2020/02/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Test
{
	/** @return Always run the class? */
	boolean alwaysRun() default false;
	
	/** @return Custom attributes. */
	CustomAttribute[] attributes() default {};
	
	/** @return Data provider. */
	String dataProvider() default "";
	
	/** @return The class for the data provider. */
	Class<?> dataProviderClass() default Object.class;
	
	/** @return The groups this depends on. */
	String[] dependsOnGroups() default {};
	
	/** @return The methods this depends on. */
	String[] dependsOnMethods() default {};
	
	/** @return The test description. */
	String description() default "";
	
	/** @return Enabled by default. */
	boolean enabled() default true;
	
	/** @return The expected excpetions. */
	Class[] expectedExceptions() default {};
	
	/** @return Regular expression for the expected exception message. */
	String expectedExceptionsMessageRegExp() default ".*";
	
	/** @return Groups the test is under. */
	String[] groups() default {};
	
	/** @return Are missing dependencies ignored? */
	boolean ignoreMissingDependencies() default false;
	
	/** @return The number of times to invoke the method. */
	int invocationCount() default 1;
	
	/** @return How long before the invocation will time out. */
	long invocationTimeOut() default 0;
	
	/** @return The test priority. */
	int priority() default 0;
	
	/** @return Are the tests single threaded? */
	boolean singleThreaded() default false;
	
	/** @return Skip methods that fail to be invoked? */
	boolean skipFailedInvocations() default false;
	
	/** @return The expected percentage of passing tests. */
	int successPercentage() default 100;
	
	/** @return The name of the suite. */
	String suiteName() default "";
	
	/** @return THe test name. */
	String testName() default "";
	
	/** @return The size of the thread pool. */
	int threadPoolSize() default 0;
	
	/** @return Maximum amount of time the test should take. */
	long timeOut() default 0;
}
