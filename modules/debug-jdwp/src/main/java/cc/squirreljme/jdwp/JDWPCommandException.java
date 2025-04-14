// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This is thrown when there is an error parsing and executing a packet.
 *
 * @since 2021/04/11
 */
public class JDWPCommandException
	extends JDWPException
{
	/** The type of error this is. */
	public final JDWPErrorType type;
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __errorType The error type.
	 * @since 2024/01/26
	 */
	public JDWPCommandException(JDWPErrorType __errorType)
	{
		this.type = (__errorType == null ? JDWPErrorType.INTERNAL :
			__errorType);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __errorType The error type.
	 * @param __m The message for the error.
	 * @since 2021/04/11
	 */
	public JDWPCommandException(JDWPErrorType __errorType, String __m)
	{
		super(__m);
		
		this.type = (__errorType == null ? JDWPErrorType.INTERNAL :
			__errorType);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __errorType The error type.
	 * @param __m The message for the error.
	 * @param __c The cause.
	 * @since 2021/04/15
	 */
	public JDWPCommandException(JDWPErrorType __errorType, String __m,
		Throwable __c)
	{
		super(__m, __c);
		
		this.type = (__errorType == null ? JDWPErrorType.INTERNAL :
			__errorType);
	}
	
	/**
	 * Generates an exception for an invalid class.
	 * 
	 * @param __obj The context.
	 * @param __cause The cause of this exception.
	 * @return The exception.
	 * @since 2021/04/15
	 */
	public static JDWPCommandException tossInvalidClass(Object __obj,
		Throwable __cause)
	{
		return JDWPErrorType.INVALID_CLASS.toss(__obj,
			System.identityHashCode(__obj), __cause);
	}
	
	/**
	 * Generates an exception for an invalid method.
	 * 
	 * @param __obj The context.
	 * @param __methodDx The method index.
	 * @param __cause The cause of this exception.
	 * @return The exception.
	 * @since 2021/04/15
	 */
	public static JDWPCommandException tossInvalidMethod(Object __obj,
		int __methodDx, Throwable __cause)
	{
		return JDWPErrorType.INVALID_METHOD_ID.toss(__obj, __methodDx,
			__cause);
	}
	
	/**
	 * Generates an exception for an invalid method.
	 * 
	 * @param __obj The context.
	 * @param __fieldDx The field index.
	 * @param __cause The cause of this exception.
	 * @return The exception.
	 * @since 2021/04/16
	 */
	public static JDWPCommandException tossInvalidField(Object __obj,
		int __fieldDx, Throwable __cause)
	{
		return JDWPErrorType.INVALID_FIELD_ID.toss(__obj, __fieldDx,
			__cause);
	}
}
