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
 * Tests that {@link String#regionMatches(boolean, int, String, int, int, int)}
 * is working.
 *
 * @since 2020/01/18
 */
public class TestStringRegionMatches
	extends TestRunnable
{
	/** String A. */
	public static final String A =
		"Hello! Squirrels are adorable!";
	
	/** String B. */
	public static final String B =
		"AdoRable squirRels are hello!";
	
	/**
	 * {@inheritDoc}
	 * @since 2020/01/18
	 */
	@Override
	public void test()
	{
		for (boolean igncase = false;; igncase = true)
		{
			char key = (igncase ? 'i' : 'c');
			
			this.secondary("same" + key,
				A.regionMatches(igncase, 0, A, 0, A.length()));
				
			this.secondary("sameover" + key,
				A.regionMatches(igncase, 0, A, 0, A.length() + 3));
			this.secondary("samehalf" + key,
				A.regionMatches(igncase, 0, A, 0, A.length() / 2));
			
			this.secondary("neglen" + key,
				A.regionMatches(igncase, 0, A, 0, -3));
			
			this.secondary("samenega" + key,
				A.regionMatches(igncase, -7, A, 7, 9));
			this.secondary("samenegb" + key,
				A.regionMatches(igncase, 7, A, -7, 9));
			
			this.secondary("hello" + key,
				A.regionMatches(igncase, 0, B, 23, 5));
			this.secondary("squirrels" + key,
				A.regionMatches(igncase, 7, B, 9, 9));
			this.secondary("are" + key,
				A.regionMatches(igncase, 17, B, 19, 3));
			this.secondary("adorable" + key,
				A.regionMatches(igncase, 21, B, 0, 8));
			
			if (igncase)
				return;
		}
	}
}

