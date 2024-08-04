// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.exceptions.SpringArithmeticException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassCastException;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassFormatException;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIllegalAccessException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIllegalMonitorStateException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIncompatibleClassChangeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchFieldException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNullPointerException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import net.multiphasicapps.classfile.ClassName;

/**
 * This is the base class for all exceptions within the spring machine.
 *
 * @since 2018/08/05
 */
public class SpringException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/08/05
	 */
	public SpringException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/08/05
	 */
	public SpringException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/08/05
	 */
	public SpringException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/08/05
	 */
	public SpringException(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * Converts an exception to SpringCoat.
	 *
	 * @param __t The throwable.
	 * @return The resultant SpringCoat exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static SpringException convert(Throwable __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Class<?> tC = __t.getClass();
		if (tC == ArithmeticException.class)
			return new SpringArithmeticException(__t);
		if (tC == ArrayIndexOutOfBoundsException.class)
			return new SpringArrayIndexOutOfBoundsException(__t);
		if (tC == ArrayStoreException.class)
			return new SpringArrayStoreException(__t);
		if (tC == ClassCastException.class)
			return new SpringClassCastException(__t);
		if (tC == ClassNotFoundException.class)
			return new SpringClassNotFoundException(
				new ClassName("Unknown"), __t);
		if (tC == IllegalAccessException.class)
			return new SpringIllegalAccessException(__t);
		if (tC == IllegalMonitorStateException.class)
			return new SpringIllegalMonitorStateException(__t);
		if (tC == MLECallError.class)
			return new SpringMLECallError(__t);
		if (tC == NegativeArraySizeException.class)
			return new SpringNegativeArraySizeException(__t);
		if (tC == NoSuchFieldException.class)
			return new SpringNoSuchFieldException(__t);
		if (tC == NoSuchMethodException.class)
			return new SpringNoSuchMethodException(__t);
		if (tC == NullPointerException.class)
			return new SpringNullPointerException(__t);
		
		// Nothing to wrap to?
		throw new SpringFatalException("Not wrappable.", __t);
	}
}

