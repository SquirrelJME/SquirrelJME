// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.MissingCollections;

/**
 * THis preresents a flag which is used for methods.
 *
 * @since 2016/03/19
 */
public enum JVMMethodFlag
	implements JVMBitFlag, JVMMemberFlag
{
	/** Public method. */
	PUBLIC(0x0001),
	
	/** Private method. */
	PRIVATE(0x0002),
	
	/** Protected method. */
	PROTECTED(0x0004),
	
	/** Static method. */
	STATIC(0x0008),
	
	/** Final method. */
	FINAL(0x0010),
	
	/** Synchronized method. */
	SYNCHRONIZED(0x0020),
	
	/** Bridge method. */
	BRIDGE(0x0040),
	
	/** Variable argument method. */
	VARARGS(0x0080),
	
	/** Native method. */
	NATIVE(0x0100),
	
	/** Abstract method. */
	ABSTRACT(0x0400),
	
	/** Strict floating point method. */
	STRICT(0x0800),
	
	/** Synthetic method. */
	SYNTHETIC(0x1000),
	
	/** End. */
	;
	
	/** The flag mask. */
	protected final int mask;
	
	/**
	 * Initializes the flag.
	 *
	 * @param __m The used mask.
	 * @throws IllegalArgumentException If not exactly a single bit is set.
	 * @since 2016/03/19
	 */
	private JVMMethodFlag(int __m)
		throws IllegalArgumentException
	{
		// Check
		if (Integer.bitCount(__m) != 1)
			throw new IllegalArgumentException(String.format("IN0b %d", __m));
		
		// Set
		mask = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public final int mask()
	{
		return mask;
	}
}

