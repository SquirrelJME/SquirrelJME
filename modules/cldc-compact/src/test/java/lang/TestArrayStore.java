// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that arrays have the proper type checking for objects when stored.
 *
 * @since 2022/06/20
 */
public class TestArrayStore
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/06/20
	 */
	@SuppressWarnings({"UnnecessaryBoxing", "StringOperationCanBeSimplified"})
	@Override
	public void test()
	{
		// Interface
		Object[] number = new Number[1]; 
		
		this.secondary("number-object",
			TestArrayStore.__store(number, new Object()));
		this.secondary("number-integer",
			TestArrayStore.__store(number, new Integer(1)));
		this.secondary("number-long",
			TestArrayStore.__store(number, new Long(1)));
		this.secondary("number-string",
			TestArrayStore.__store(number, new String("hi")));
		
		// Class but another implements similar interface
		// This one being CharSequence
		Object[] similar = new StringBuilder[1];
		
		this.secondary("similar-object",
			TestArrayStore.__store(similar, new Object()));
		this.secondary("similar-stringbuilder",
			TestArrayStore.__store(similar, new StringBuilder()));
		this.secondary("similar-stringbuffer",
			TestArrayStore.__store(similar, new StringBuffer()));
		this.secondary("similar-string",
			TestArrayStore.__store(similar, new StringBuffer()));
		
		// Extended classes
		Object[] exception = new Exception[1];
		
		this.secondary("exception-object",
			TestArrayStore.__store(exception, new Object()));
		this.secondary("exception-throwable",
			TestArrayStore.__store(exception, new Throwable()));
		this.secondary("exception-exception",
			TestArrayStore.__store(exception, new Exception()));
		this.secondary("exception-rtexception",
			TestArrayStore.__store(exception, new RuntimeException()));
		this.secondary("exception-error",
			TestArrayStore.__store(exception, new Error()));
	}
    
    /**
     * Attempts to store the given object into the specified array.
     * 
     * @param __array The array to store into.
     * @param __what The object to store.
     * @return If this threw an {@link ArrayStoreException}.
     * @throws NullPointerException On null arguments.
     * @since 2022/06/20
     */
	private static boolean __store(Object[] __array, Object __what)
		throws NullPointerException
	{
		if (__array == null || __what == null)
			throw new NullPointerException("NARG");
		
		try
		{
			__array[0] = __what;
			return false;
		}
		catch (ArrayStoreException ignored)
		{
			return true;
		}
	}
}
