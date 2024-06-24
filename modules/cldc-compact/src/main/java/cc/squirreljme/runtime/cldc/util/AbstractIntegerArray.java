// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Abstract implementation of {@link IntegerArray}, two of these arrays are
 * considered equal even if their type varies.
 *
 * @since 2023/08/09
 */
public abstract class AbstractIntegerArray
	implements IntegerArray
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof IntegerArray))
			return false;
		
		// Mismatched size?
		IntegerArray o = (IntegerArray)__o;
		int n = this.size();
		if (n != o.size())
			return false;
		
		// Compare individual values
		for (int i = 0; i < n; i++)
			if (this.get(i) != o.get(i))
				return false;
		
		// Is equal
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public final int hashCode()
	{
		int rv = 1;
		
		// Same as ArrayList, note hashCode of Integer is its own value
		for (int i = 0, n = this.size(); i < n; i++)
			rv = 31 * rv + this.get(i);
		
		return rv;
	}
}
