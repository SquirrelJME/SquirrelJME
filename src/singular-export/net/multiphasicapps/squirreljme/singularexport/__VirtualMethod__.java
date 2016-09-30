// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;

/**
 * This decodes a method.
 *
 * @since 2016/09/30
 */
class __VirtualMethod__
	implements MethodDescriptionStream
{
	/** The method this holds information for. */
	final __Method__ _method;
	
	/**
	 * Initializes the virtual method decoder.
	 *
	 * @param __m The method to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	__VirtualMethod__(__Method__ __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._method = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public CodeDescriptionStream code()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void endMember()
	{
		// Ignore
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/30
	 */
	@Override
	public void noCode()
	{
		// Ignore
	}
}

