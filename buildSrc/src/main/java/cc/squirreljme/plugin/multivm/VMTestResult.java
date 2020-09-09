// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * This represents the result of a test.
 *
 * @since 2020/09/07
 */
public enum VMTestResult
{
	/** Pass. */
	PASS,
	
	/** Fail. */
	FAIL,
	
	/** Skip. */
	SKIP,
	
	/* End. */
	;
	
	/**
	 * Decodes the exit value and returns the test result.
	 * 
	 * @param __exitValue The exit value to decode.
	 * @return The test result.
	 * @since 2020/09/07
	 */
	public static VMTestResult valueOf(int __exitValue)
	{
		if (__exitValue == 0)
			return VMTestResult.PASS;
		else if (__exitValue == 2)
			return VMTestResult.SKIP;
		return VMTestResult.FAIL;
	} 
}
