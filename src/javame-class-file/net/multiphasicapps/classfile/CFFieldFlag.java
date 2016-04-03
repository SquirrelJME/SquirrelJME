// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This represents a flag which is used for fields.
 *
 * @since 2016/03/19
 */
public enum CFFieldFlag
	implements JVMBitFlag, CFMemberFlag
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
	private static volatile Reference<List<CFFieldFlag>> _FLAGS;
	
	/** The flag mask. */
	protected final int mask;
	
	/**
	 * Initializes the flag.
	 *
	 * @param __m The used mask.
	 * @throws IllegalArgumentException If not exactly a single bit is set.
	 * @since 2016/03/19
	 */
	private CFFieldFlag(int __m)
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
	
	/**
	 * Returns all available field flags.
	 *
	 * @return The available field flags.
	 * @since 2016/03/19
	 */
	public static List<CFFieldFlag> allFlags()
	{
		// Get reference
		Reference<List<CFFieldFlag>> ref = _FLAGS;
		List<CFFieldFlag> rv;
		
		// Check it
		if (ref == null || null == (rv = ref.get()))
			_FLAGS = new WeakReference<>((rv = MissingCollections.
				<CFFieldFlag>unmodifiableList(
					Arrays.<CFFieldFlag>asList(values()))));
		
		// Return it
		return rv;
	}
}

