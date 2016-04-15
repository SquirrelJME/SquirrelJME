// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This represents the type of invocation that is to be performed.
 *
 * @since 2016/04/15
 */
public enum CPInvokeType
{
	/** Virtual. */
	VIRTUAL(true),
	
	/** Static. */
	STATIC(false),
	
	/** Special. */
	SPECIAL(true),
	
	/** Interface. */
	INTERFACE(true),
	
	/** End. */
	;
	
	/** Is this an instance invocation? */
	protected boolean isinstance;
	
	/**
	 * Initializes the invocation type.
	 *
	 * @param __i Is this an instance invocation?
	 * @since 2016/04/15
	 */
	private CPInvokeType(boolean __i)
	{
		isinstance = __i;
	}
	
	/**
	 * Is this an instance invocation?
	 *
	 * @return {@code true} if this is an instance invocation.
	 */
	public boolean isInstance()
	{
		return isinstance;
	}
}

