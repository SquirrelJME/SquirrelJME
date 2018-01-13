// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.interpreter;

import java.lang.ref.Reference;
import cc.squirreljme.jit.cff.ClassName;

/**
 * This represents an instance of an object within the virtual machine.
 *
 * @since 2017/10/06
 */
public class Instance
{
	/** The owning process. */
	private final Reference<VMProcess> _processref;
	
	/**
	 * Initializes the instance of the object.
	 *
	 * @param __p The owning process.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	Instance(Reference<VMProcess> __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this._processref = __p;
	}
	
	/**
	 * Returns the process which owns this thread.
	 *
	 * @return The owning process.
	 * @throws IllegalStateException If the process has been garbage
	 * collected.
	 * @since 2017/10/08
	 */
	final VMProcess __process()
		throws IllegalStateException
	{
		// {@squirreljme.error AH01 The process has been garbage collected.}
		VMProcess rv = this._processref.get();
		if (rv == null)
			throw new IllegalStateException("AH01");
		return rv;
	}
}

