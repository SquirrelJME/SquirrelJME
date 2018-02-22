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

import cc.squirreljme.jit.library.Library;

/**
 * This wraps a binary and provides a library used by the JIT.
 *
 * @since 2018/02/21
 */
public final class BinaryLibrary
	extends Library
{
	/** The binary to wrap. */
	protected final Binary binary;
	
	/**
	 * Initializes the library.
	 *
	 * @param __bin The binary to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public BinaryLibrary(Binary __bin)
		throws NullPointerException
	{
		if (__bin == null)
			throw new NullPointerException("NARG");
		
		this.binary = __bin;	
	}
}

