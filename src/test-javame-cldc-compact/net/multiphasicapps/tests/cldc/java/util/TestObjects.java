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

import java.util.Objects;
import net.multiphasicapps.tests.TestChecker;
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
	public String invokerName()
	{
		return "java.util.Objects";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/12
	 */
	@Override
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException("NARG");
		
		// Same null values
		__tc.checkEquals("equals.nullnull", true,
			Objects.equals(null, null));
		
		// Null and integer
		__tc.checkEquals("equals.nullint", false,
			Objects.equals(null, new Integer(42)));
		
		// Integer and null
		__tc.checkEquals("equals.intnull", false,
			Objects.equals(new Integer(42), null));
		
		// Both integers with the same value
		__tc.checkEquals("equals.intintsame", true,
			Objects.equals(new Integer(42), new Integer(42)));
		
		// Both integers with different values
		__tc.checkEquals("equals.intintdiff", false,
			Objects.equals(new Integer(42), new Integer(24)));
		
		// Integer and long, with the same value
		__tc.checkEquals("equals.intlongsame", false,
			Objects.equals(new Integer(42), new Long(42)));
		
		// Float and double, with the same value
		__tc.checkEquals("equals.floatdoublesame", false,
			Objects.equals(new Float(42.0F), new Double(42.0D)));
		
		// Hashcode of null
		__tc.checkEquals("hashcode.null", 0,
			Objects.hashCode(null));
		
		// Hashcode of integer value
		__tc.checkEquals("hashcode.int", new Integer(42),
			Objects.hashCode(new Integer(42)));
		
		// Require non-null value
		__tc.checkEquals("requireNonNull.notnull", true,
			Objects.<Boolean>requireNonNull(true));
		
		// Check null
		try
		{
			Objects.<Object>requireNonNull(null);
			
			// Failed
			__tc.checkEquals("requireNonNull.null", true,
				false);
		}
		
		// It worked
		catch (NullPointerException e)
		{
			__tc.checkEquals("requireNonNull.null", true,
				true);
		}
	}
}

