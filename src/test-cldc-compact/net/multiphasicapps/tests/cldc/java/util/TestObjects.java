// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.cldc.java.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This tests that {@link Objects} is implemented correctly.
 *
 * @since 2016/04/12
 */
public class TestObjects
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/12
	 */
	@Override
	public TestGroupName invokerName()
	{
		return new TestGroupName("java.util.Objects");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public Iterable<String> invokerTests()
	{
		return Arrays.<String>asList(
			// Comparisons
			"compare.lessthan.int.1.2",
			"compare.equals.int.1.1",
			"compare.greaterthan.int.2.1",
			
			// Equality
			"equals.equals.int.1.int.1",
			"equals.notequals.int.1.long.1",
			"equals.notequals.int.1.int.2",
			
			// Require non-null
			"requirenonnull.pass.int",
			"requirenonnull.fail.null",
			
			// Convert to strng
			"tostring.equals.hello.hello",
			"tostring.equals.null.null",
			"tostring.notequals.null.hello",
			
			// Default value
			"tostring_default.equals.hello.bye.hello",
			"tostring_default.equals.null.bye.bye"
			);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/12
	 */
	@Override
	public void runTest(TestChecker __tc, String __st)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __st == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This is the type of check which may be performed.
	 *
	 * @since 2016/05/05
	 */
	private static enum __CheckType__
	{
		/** Compare two values. */
		COMPARE,
		
		/** Equals each other. */
		EQUALS,
		
		/** Hash code of an object. */
		HASHCODE,
		
		/** Require value not to be null. */
		REQUIRENONNULL,
		
		/** Convert to string. */
		TOSTRING,
		
		/** Convert to string, with default. */
		TOSTRING_DEFAULT,
		
		/** End. */
		;
	}
}

