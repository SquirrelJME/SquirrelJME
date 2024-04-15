// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * Results on test information.
 *
 * @since 2020/11/26
 */
public final class ResultantTestInfo
{
	/** The name of the test. */
	public final String name;
	
	/** The result of the test, if it passed or not. */
	public final VMTestResult result;
	
	/** The duration of the test. */
	public final long nanoseconds;
	
	/**
	 * Stores the resultant test information.
	 * 
	 * @param __name The name of the test.
	 * @param __result The result of the test.
	 * @param __nanoseconds The nanoseconds of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/26
	 */
	public ResultantTestInfo(String __name, VMTestResult __result,
		long __nanoseconds)
		throws NullPointerException
	{
		if (__name == null || __result == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.result = __result;
		this.nanoseconds = __nanoseconds;
	}
}
