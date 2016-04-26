// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import net.multiphasicapps.narf.exception.NARFException;

/**
 * This is thrown when a class is attempted to be read however it cannot be
 * read properly because of a disk error or it is malformed.
 *
 * @since 2016/04/21
 */
public class NCIException
	extends NARFException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/04/21
	 */
	public NCIException(Issue __i)
	{
		super(__i);
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/04/21
	 */
	public NCIException(Issue __i, String __msg)
	{
		super(__i, __msg);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/04/21
	 */
	public NCIException(Issue __i, String __msg, Throwable __c)
	{
		super(__i, __msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/04/21
	 */
	public NCIException(Issue __i, Throwable __c)
	{
		super(__i, __c);
	}
	
	/**
	 * This represents 
	 *
	 * @since 2016/04/23
	 */
	public static enum Issue
		implements BaseIssue
	{
		/** A class was not found. */
		CLASS_NOT_FOUND,
		
		/** Class failed to be read. */
		READ_ERROR,
		
		/** Illegal flags. */
		ILLEGAL_FLAGS,
		
		/** Native method. */
		NATIVE_METHOD,
		
		/** Illegal class magic number. */
		BAD_MAGIC_NUMBER,
		
		/** The class version is not supported or correct. */
		BAD_CLASS_VERSION,
		
		/** The constant pool has an invalid size. */
		INVALID_POOL_SIZE,
		
		/** Illegal constant pool tag. */
		ILLEGAL_TAG,
		
		/** Invokedynamic is used. */
		INVOKEDYNAMIC,
		
		/** Illegal modified UTF-8 sequence. */
		ILLEGAL_MUTF,
		
		/** A constant pool entry is not valid. */
		ILLEGAL_CONSTANT,
		
		/** The wrong constant is here. */
		WRONG_CONSTANT,
		
		/** The name of a class is not valid. */
		BAD_CLASS_NAME,
		
		/** Expected the end of the class, however there is still data. */
		NOT_EOC,
		
		/** Attribute length is negative. */
		NEGATIVE_ATTRIBUTE,
		
		/** Field has multiple constant values. */
		DOUBLE_CONST,
		
		/** The constant of a field is not valid. */
		BAD_CONSTANT,
		
		/** The method has multiple code attributes. */
		DOUBLE_CODE,
		
		/** Class has a duplicated field. */
		DUPLICATE_FIELD,
		
		/** Class has a duplicated method. */
		DUPLICATE_METHOD,
		
		/** Short read of the code attribute. */
		SHORT_CODE,
		
		/** Abstract/Native method has code. */
		ABSTRACT_CODE,
		
		/** Non-abstract/non-native method is missing code. */
		MISSING_CODE,
		
		/** The name for a method is not valid. */
		INVALID_METHOD_NAME,
		
		/** End. */
		;
	}
}

