// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the type of invocation to be performed.
 *
 * @since 2016/09/05
 */
public enum ClassMethodInvokeType
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
	private ClassMethodInvokeType(boolean __ii)
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

