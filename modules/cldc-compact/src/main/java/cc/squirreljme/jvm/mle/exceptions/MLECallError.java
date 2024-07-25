// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.exceptions;

import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is thrown when there was an error made during a MLE call.
 *
 * @since 2020/06/22
 */
@SquirrelJMEVendorApi
public class MLECallError
	extends VirtualMachineError
{
	/** the distinctive error code. */
	public final int distinction;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError()
	{
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m)
	{
		super(__m);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, Throwable __t)
	{
		super(__m);
		
		this.initCause(__t);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public MLECallError(Throwable __t)
	{
		this.initCause(__t);
		
		this.distinction = 0;
	}
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(int __dist)
	{
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, int __dist)
	{
		super(__m);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(String __m, Throwable __t, int __dist)
	{
		super(__m);
		
		this.initCause(__t);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @param __dist The distinction used.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public MLECallError(Throwable __t, int __dist)
	{
		this.initCause(__t);
		
		this.distinction = __dist;
	}
	
	/**
	 * Initializes system call exception.
	 * 
	 * @param __callId The call ID.
	 * @param __code The {@link SystemCallError}.
	 * @since 2020/11/29
	 */
	@SquirrelJMEVendorApi
	public MLECallError(int __callId, int __code)
	{
		/* {@squirreljme.error ZZ4k Failed system call. (The ID; The Error)} */
		super("ZZ4k " + __callId + " " + __code);
		
		this.distinction = 0;
	}
	
	/**
	 * Throws a distinct exception based on the error type.
	 *
	 * @return Never returns, an exception is always thrown.
	 * @throws Error If an error or the distinction has no matching
	 * Java exception.
	 * @throws RuntimeException If the exception is a {@link RuntimeException}
	 * or would end up being a checked exception.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	public RuntimeException throwDistinct()
		throws Error, RuntimeException
	{
		// Message to base off
		String message = this.getMessage();
		
		// Determine what to toss
		Throwable toss = null;
		switch (this.distinction)
		{
			case MLECallErrorCode.CLASS_CAST:
				toss = new ClassCastException(message);
				break;
				
			case MLECallErrorCode.INDEX_OUT_OF_BOUNDS:
				toss = new IndexOutOfBoundsException(message);
				break;
				
			case MLECallErrorCode.ILLEGAL_STATE:
				toss = new IllegalStateException(message);
				break;
				
			case MLECallErrorCode.INVALID_ARGUMENT:
				toss = new IllegalArgumentException(message);
				break;
				
			case MLECallErrorCode.INVALID_THREAD_STATE:
				toss = new IllegalThreadStateException(message);
				break;
				
			case MLECallErrorCode.NULL_ARGUMENTS:
				toss = new NullPointerException(message);
				break;
				
			case MLECallErrorCode.STACK_OVERFLOW:
				toss = new StackOverflowError(message);
				break;
				
			case MLECallErrorCode.UNSUPPORTED_OPERATION:
				toss = new UnsupportedOperationException(message);
				break;
				
			case MLECallErrorCode.CANNOT_CREATE:
			case MLECallErrorCode.OUT_OF_MEMORY:
			case MLECallErrorCode.POOL_INIT_FAILED:
				toss = new OutOfMemoryError(message);
				break;
				
			case MLECallErrorCode.COULD_NOT_LOAD_LIBRARY:
			case MLECallErrorCode.INVALID_LIBRARY_SYMBOL:
			case MLECallErrorCode.LIBRARY_NOT_FOUND:
				toss = new LinkageError(message);
				break;
				
			case MLECallErrorCode.INVALID_BINARY_NAME:
			case MLECallErrorCode.INVALID_IDENTIFIER:
			case MLECallErrorCode.INVALID_FIELD_TYPE:
			case MLECallErrorCode.INVALID_METHOD_TYPE:
			case MLECallErrorCode.INVALID_REFERENCE_POP:
			case MLECallErrorCode.INVALID_REFERENCE_PUSH:
			case MLECallErrorCode.LOCAL_INDEX_INVALID:
			case MLECallErrorCode.LOCAL_INVALID_READ:
			case MLECallErrorCode.LOCAL_INVALID_WRITE:
			case MLECallErrorCode.STACK_INDEX_INVALID:
			case MLECallErrorCode.STACK_INVALID_READ:
			case MLECallErrorCode.STACK_INVALID_WRITE:
			case MLECallErrorCode.TOP_NOT_DOUBLE:
			case MLECallErrorCode.TOP_NOT_FLOAT:
			case MLECallErrorCode.TOP_NOT_INTEGER:
			case MLECallErrorCode.TOP_NOT_LONG:
			case MLECallErrorCode.TOP_NOT_OBJECT:
				toss = new ClassFormatError(message);
				break;
		}
		
		// Tossing something different?
		if (toss != null)
		{
			// Use our original message and cause, if any
			toss.initCause(this.getCause());
			
			// Use our original stack trace
			toss.setStackTrace(this.getStackTrace());
			
			// Throw directly or wrap
			if (toss instanceof RuntimeException)
				throw (RuntimeException)toss;
			else if (toss instanceof Error)
				throw (Error)toss;
			else
				throw new RuntimeException("Checked Exception", toss);
		}
		
		// Unknown, just throw self
		throw this;
	}
}
