// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * The type of comparison to make for a test.
 *
 * @since 2016/07/12
 */
public enum TestComparison
{
	/** Equal. */
	EQUALS,
	
	/** Not equals. */
	NOT_EQUALS,
	
	/** Less than. */
	LESS_THAN,
	
	/** Less than or equal to. */
	AT_MOST,
	
	/** Greater than. */
	GREATER_THAN,
	
	/** More than or equal to. */
	AT_LEAST,
	
	/** End. */
	;
	
	/**
	 * Checks if the given comparison is passing or failing for this given
	 * comparison.
	 *
	 * @param __comp The comparison to make.
	 * @return Whether it passed or failed.
	 * @since 2016/07/14
	 */
	final TestPassState __passState(int __comp)
	{
		// Depends on the check
		boolean pass;
		switch (this)
		{
			case EQUALS: pass = (__comp == 0); break;
			case NOT_EQUALS: pass = (__comp != 0); break;
			case LESS_THAN: pass = (__comp < 0); break;
			case AT_MOST: pass = (__comp <= 0); break;
			case GREATER_THAN: pass = (__comp > 0); break;
			case AT_LEAST: pass = (__comp >= 0); break;
			
				// Unknown
			default:
				throw new RuntimeException(String.format("OOPS %s", this));
		}
		
		// Passed or failed?
		return (pass ? TestPassState.PASS : TestPassState.FAIL);
	}
}

