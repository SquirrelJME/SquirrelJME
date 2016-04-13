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

import java.util.Comparator;
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
		
		// Setup integer comparison
		Comparator<Integer> comp = new __IntComparator__();
		Comparator<Integer> cnul = new __IntNoNullComparator__();
		Comparator<Integer> cbig = new __IntAlwaysBigComparator__();
		
		// Compare lower
		__tc.checkEquals("compare.intintlow", -1,
			Integer.signum(Objects.compare(new Integer(1), new Integer(2),
				comp)));
		
		// Compare higher
		__tc.checkEquals("compare.intinthigh", 1,
			Integer.signum(Objects.compare(new Integer(2), new Integer(1),
				comp)));
		
		// Compare same
		__tc.checkEquals("compare.intintsame", 0,
			Integer.signum(Objects.compare(new Integer(1), new Integer(1),
				comp)));
		
		// Compare two nulls
		__tc.checkEquals("compare.nullnull", 0,
			Integer.signum(Objects.compare(null, null,
				comp)));
		
		// Compare two nulls
		__tc.checkEquals("compare.antinullnull", 0,
			Integer.signum(Objects.compare(null, null,
				cnul)));
		
		// Compare similar valued integers but always greater
		__tc.checkEquals("compare.alwaysintintsameobjdif", 1,
			Integer.signum(Objects.compare(new Integer(1), new Integer(1),
				cbig)));
		
		// Compare the same object
		Integer q = new Integer(1);
		__tc.checkEquals("compare.alwaysintintsameobjsame", 0,
			Integer.signum(Objects.compare(q, q,
				cbig)));
		
		// Compare similar valued integers but always greater
		try
		{
			Objects.compare(new Integer(1), new Integer(1), null);
			
			// Failed
			__tc.checkEquals("compare.alwaysintintsameobjdifnullc", true,
				false);
		}
		
		// Success
		catch (NullPointerException e)
		{
			__tc.checkEquals("compare.alwaysintintsameobjdifnullc", true,
				true);
		}
		
		// Compare the same object
		q = new Integer(1);
		__tc.checkEquals("compare.alwaysintintsameobjsamenullc", 0,
			Integer.signum(Objects.compare(q, q,
				null)));
		
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
		
		// To string on a value
		__tc.checkEquals("tostring.notnull", "hello",
			Objects.toString("hello"));
		
		// To string on null
		__tc.checkEquals("tostring.null", "null",
			Objects.toString(null));
		
		// To string on a value with an alternative
		__tc.checkEquals("tostring.altnotnull", "hello",
			Objects.toString("hello", "bye"));
		
		// To string on null with alternative
		__tc.checkEquals("tostring.altnull", "bye",
			Objects.toString(null, "bye"));
	}
	
	/**
	 * This is a comparator in which comparisons are always larger.
	 *
	 * @since 2016/04/12
	 */
	private static class __IntAlwaysBigComparator__
		implements Comparator<Integer>
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/04/12
		 */
		@Override
		public int compare(Integer __a, Integer __b)
		{
			return 1;
		}
	}
	
	/**
	 * Compares two integer values.
	 *
	 * @since 2016/04/12
	 */
	private static class __IntComparator__
		implements Comparator<Integer>
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/04/12
		 */
		@Override
		public int compare(Integer __a, Integer __b)
		{
			boolean na = (__a == null),
				nb = (__b == null);
			if (na && !nb)
				return -1;
			else if (!na && nb)
				return 1;
			else if (na && nb)
				return 0;
			
			int a = __a.intValue(),
				b = __b.intValue();
			
			if (a < b)
				return -1;
			else if (a > b)
				return 1;
			return 0;
		}
	}
	
	/**
	 * This compares integers, however it does not accept null values.
	 *
	 * @since 2016/04/12
	 */
	private static class __IntNoNullComparator__
		implements Comparator<Integer>
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/04/12
		 */
		@Override
		public int compare(Integer __a, Integer __b)
		{
			if (__a == null || __b == null)
				throw new NullPointerException("NARG");
			
			int a = __a.intValue(),
				b = __b.intValue();
			
			if (a < b)
				return -1;
			else if (a > b)
				return 1;
			return 0;
		}
	}
}

