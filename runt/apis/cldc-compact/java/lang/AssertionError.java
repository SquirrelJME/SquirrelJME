// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is thrown when an assertion check fails.
 *
 * @since 2018/12/04
 */
public class AssertionError
	extends Error
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public AssertionError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(Object __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(boolean __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(char __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(int __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(long __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(float __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public AssertionError(double __m)
	{
		super(AssertionError.__convert(__m));
	}
	
	/**
	 * Converts the given object to a string.
	 *
	 * @param __o The object to convert.
	 * @return The string form of the object.
	 * @since 2018/12/04
	 */
	private static final String __convert(Object __o)
	{
		if (__o == null)
			return "null";
		
		// Just make sure we can convert this
		try
		{
			return __o.toString();
		}
		
		// Could not convert
		catch (Throwable t)
		{
			// Debug it
			t.printStackTrace();
			
			// {@squirreljme.error ZZ34 Assertion string conversion failed
			// with no message.}
			String m = t.getMessage();
			return (m == null ? "ZZ34" : m);
		}
	}
}

