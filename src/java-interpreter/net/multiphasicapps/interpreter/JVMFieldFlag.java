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
 * This represents a flag which is used for fields.
 *
 * @since 2016/03/19
 */
public enum JVMFieldFlag
	implements JVMBitFlag, JVMMemberFlag
{
	/** Public field. */
	PUBLIC(0x0001),
	
	/** Private field. */
	PRIVATE(0x0002),
	
	/** Protected field. */
	PROTECTED(0x0004),
	
	/** Static field. */
	STATIC(0x0008),
	
	/** Final field. */
	FINAL(0x0010),
	
	/** Volatile field. */
	VOLATILE(0x0040),
	
	/** Transient field. */
	TRANSIENT(0x0080),
	
	/** Synthetic field. */
	SYNTHETIC(0x1000),
	
	/** Enumeration. */
	ENUM(0x4000),
	
	/** End. */
	;
	
	/** All available flags. */
	public static final List<JVMFieldFlag> FLAGS =
		MissingCollections.<JVMFieldFlag>unmodifiableList(
		Arrays.<JVMFieldFlag>asList(values()));
	
	/** The flag mask. */
	public final int mask;
	
	/**
	 * Initializes the flag.
	 *
	 * @param __m The used mask.
	 * @throws IllegalArgumentException If not exactly a single bit is set.
	 * @since 2016/03/19
	 */
	private JVMFieldFlag(int __m)
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

