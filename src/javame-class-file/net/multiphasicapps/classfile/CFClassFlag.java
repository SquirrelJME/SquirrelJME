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
 * Class flags.
 *
 * @since 2016/03/15
 */
public enum CFClassFlag
	implements CFBitFlag
{
	/** Public access. */
	PUBLIC(0x0001),
	
	/** Final. */
	FINAL(0x0010),
	
	/** Super. */
	SUPER(0x0020),
	
	/** Interface. */
	INTERFACE(0x0200),
	
	/** Abstract. */
	ABSTRACT(0x0400),
	
	/** Synthetic. */
	SYNTHETIC(0x1000),
	
	/** Annotation. */
	ANNOTATION(0x2000),
	
	/** Enumeration. */
	ENUM(0x4000),
	
	/** End. */
	;
	
	/** All available flags. */
	private static volatile Reference<List<CFClassFlag>> _FLAGS;
	
	/** The flag mask. */
	public final int mask;
	
	/**
	 * Initializes the flag.
	 *
	 * @param __m The used mask.
	 * @throws IllegalArgumentException If not exactly a single bit is set.
	 * @since 2016/03/15
	 */
	private CFClassFlag(int __m)
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
	 * @since 2016/03/15
	 */
	@Override
	public final int mask()
	{
		return mask;
	}
	
	/**
	 * Returns all available class flags.
	 *
	 * @return The available class flags.
	 * @since 2016/03/19
	 */
	public static List<CFClassFlag> allFlags()
	{
		// Get reference
		Reference<List<CFClassFlag>> ref = _FLAGS;
		List<CFClassFlag> rv;
		
		// Check it
		if (ref == null || null == (rv = ref.get()))
			_FLAGS = new WeakReference<>((rv = MissingCollections.
				<CFClassFlag>unmodifiableList(
					Arrays.<CFClassFlag>asList(values()))));
		
		// Return it
		return rv;
	}
}

