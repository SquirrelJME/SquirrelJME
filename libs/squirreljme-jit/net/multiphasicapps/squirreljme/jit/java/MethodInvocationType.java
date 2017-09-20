// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.hil.MethodLookupType;

/**
 * This represents the type of invocation that may be performed on a method.
 *
 * @since 2017/09/16
 */
public enum MethodInvocationType
{
	/** Interface method. */
	INTERFACE,
	
	/** Special call. */
	SPECIAL,
	
	/** Static call. */
	STATIC,
	
	/** Virtual call. */
	VIRTUAL,
	
	/** End. */
	;
	
	/**
	 * Is this an instance call?
	 *
	 * @return If this is an instance call or not.
	 * @since 2017/09/16
	 */
	public final boolean isInstance()
	{
		return this == INTERFACE || this == VIRTUAL || this == SPECIAL;
	}
	
	/**
	 * This returns the type of lookup to perform when the method is to be
	 * invoked.
	 *
	 * @return The type of lookup to perform.
	 * @since 2017/09/20
	 */
	public final MethodLookupType lookupType()
	{
		switch (this)
		{
			case INTERFACE:
			case VIRTUAL:
				return MethodLookupType.VIRTUAL;
			
			case SPECIAL:
				return MethodLookupType.SPECIAL;
			
			case STATIC:
				return MethodLookupType.EXACT;
			
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

