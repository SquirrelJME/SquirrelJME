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

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;

/**
 * This represents the state of the stack cache.
 *
 * @since 2016/09/16
 */
class __CacheState__
{
	/** The state of the stack. */
	final CodeVariable[] _stack;
	
	/**
	 * Initializes the cache state.
	 *
	 * @param __s The size of the stack.
	 * @since 2016/09/16
	 */
	__CacheState__(int __s)
	{
		// Allocate
		this._stack = new CodeVariable[__s];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public String toString()
	{
		return Arrays.<CodeVariable>asList(this._stack).toString();
	}
}

