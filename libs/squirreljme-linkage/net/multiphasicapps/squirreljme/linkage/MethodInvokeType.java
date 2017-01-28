// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

/**
 * This represents the type of invocation to be performed.
 *
 * @since 2016/09/05
 */
public enum MethodInvokeType
{
	/** Static. */
	STATIC(false),
	
	/** Virtual. */
	VIRTUAL(true),
	
	/** Special. */
	SPECIAL(true),
	
	/** Interface. */
	INTERFACE(true),
	
	/** End. */
	;
	
	/** Is this an instance invocation? */
	protected final boolean isinstance;
	
	/**
	 * Initializes the invocation type.
	 *
	 * @param __ii Is this an instance invoke?
	 * @since 2016/09/06
	 */
	private MethodInvokeType(boolean __ii)
	{
		this.isinstance = __ii;
	}
	
	/**
	 * Returns {@code true} if this is an instance invocation.
	 *
	 * @return {@code true} if an instance invocation.
	 * @since 2016/09/06
	 */
	public final boolean isInstance()
	{
		return this.isinstance;
	}
	
	/**
	 * Returns {@code true} if this is a static invocation.
	 *
	 * @return {@code true} if a static invocation.
	 * @since 2016/09/06
	 */
	public final boolean isStatic()
	{
		return !this.isinstance;
	}
}

