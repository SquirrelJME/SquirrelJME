// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

/**
 * This specifies the type of timespace to select when looking for projects.
 *
 * @since 2017/11/14
 */
public enum TimeSpaceType
{
	/** Runtime. */
	RUNTIME,
	
	/** JIT time. */
	JIT,
	
	/** Testing. */
	TEST,
	
	/** Build time. */
	BUILD,
	
	/** End. */
	;
	
	/**
	 * Returns the timespace type for the given string.
	 *
	 * @param __s The input string.
	 * @return The timespace for the given input or {@code null} if there is
	 * no such timespace.
	 * @since 2017/11/16
	 */
	public static TimeSpaceType ofString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		switch (__s)
		{
			case "runtime":		return RUNTIME;
			case "jit":			return JIT;
			case "test":		return TEST;
			case "build":		return BUILD;
			
			default:
				return null;
		}
	}
}

