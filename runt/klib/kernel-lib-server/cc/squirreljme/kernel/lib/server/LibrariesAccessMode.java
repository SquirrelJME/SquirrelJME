// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.server;

import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;

/**
 * This represents the type of access which can be performed.
 *
 * @since 2018/01/11
 */
public enum LibrariesAccessMode
{
	/** None. */
	NONE,
	
	/** The current group only. */
	SAME_GROUP,
	
	/** Any group. */
	ANY,
	
	/** End. */
	;
	
	/**
	 * Checks if the access from the two groups is possible.
	 *
	 * @param __from The from group.
	 * @param __to The to group.
	 * @return If access is permitted.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/11
	 */
	public final boolean isAccessible(SystemTrustGroup __from,
		SystemTrustGroup __to)
		throws NullPointerException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		switch (this)
		{
				// No access at all
			case NONE:
				return false;
				
				// Only in the same group
			case SAME_GROUP:
				return __from.equals(__to);
				
				// Allow access to any
			case ANY:
				return true;
			
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

