// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This provides an interface to the constant pool of a class which provides
 * a nicer interface for the output class format.
 *
 * @since 2016/08/12
 */
public final class JITConstantPool
{
	/**
	 * Initializes the constant pool representation.
	 *
	 * @param __dc The owning class decoder.
	 * @param __pool The constant pool to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	JITConstantPool(__ClassDecoder__ __dc, __ClassPool__ __pool)
		throws NullPointerException
	{
		// Check
		if (__dc == null || __pool == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

