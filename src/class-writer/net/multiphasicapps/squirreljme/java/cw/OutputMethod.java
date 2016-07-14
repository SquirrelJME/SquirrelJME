// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.cw;

import net.multiphasicapps.squirreljme.java.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMethodID;

/**
 * This is used to describe an output method.
 *
 * @since 2016/06/21
 */
public final class OutputMethod
	extends OutputMember<CIMethodID, CIMethodFlags>
{
	/** The current code the method uses. */
	private volatile OutputCode _code;
	
	/**
	 * Initializes the output method.
	 *
	 * @param __oc The owning class.
	 * @param __id The method identifier.
	 * @since 2016/06/21
	 */
	OutputMethod(OutputClass __oc, CIMethodID __id)
	{
		super(__oc, __id, CIMethodFlags.class);
	}
	
	/**
	 * Creates a new block of mutable and output byte code that the method
	 * uses during execution.
	 *
	 * @return The output code attribute.
	 * @since 2016/06/21
	 */
	public final OutputCode createCode()
	{
		// Lock
		synchronized (this.lock)
		{
			OutputCode rv = this._code;
			if (rv == null)
				this._code = (rv = new OutputCode(this));
			return rv;
		}
	}
}

