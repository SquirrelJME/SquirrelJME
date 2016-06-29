// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

/**
 * This represents the class verion that a class may be.
 *
 * @since 2016/06/29
 */
enum __ClassVersion__
{
	/** CLDC 1.0 (JSR 30). */
	CLDC_1((45 << 16) + 3, false, false, false),
	
	/** CLDC 1.1 (JSR 139). */
	CLDC_1_1((47 << 16), true, false, false),
	
	/** CLDC 8 (aka Java 7). */
	CLDC_8((51 << 16), true, false, true),
	
	/** End. */
	;
	
	/** The minimum supported version. */
	public static final __ClassVersion__ MIN_VERSION =
		CLDC_1;
	
	/** The maximum supported version. */
	public static final __ClassVersion__ MAX_VERSION =
		CLDC_8;
	
	/** The version ID. */
	protected final int version;
	
	/** Has floating point support? */
	protected final boolean hasfloat;
	
	/** Supports invokedynamic? */
	protected final boolean hasinvokedynamic;
	
	/** Use StackMapTable? */
	protected final boolean usestackmaptable;
	
	/**
	 * Initializes the version data.
	 *
	 * @param __vid The version ID.
	 * @param __undef Is this version information undefined?
	 * @param __float Is floating point supported?
	 * @param __hasid Has invoke dynamic support?
	 * @param __usesmt Should the StackMapTable attribute be used?
	 * @since 2016/03/13
	 */
	private __ClassVersion__(int __vid,
		boolean __float, boolean __hasid, boolean __usesmt)
	{
		// Set
		version = __vid;
		hasfloat = __float;
		hasinvokedynamic = __hasid;
		usestackmaptable = __usesmt;
	}
	
	/**
	 * Returns {@code true} if floating point is supported by the virtual
	 * machine.
	 *
	 * @return {@code true} if it is.
	 * @since 2016/03/13
	 */
	public boolean hasFloatingPoint()
	{
		return hasfloat;
	}
	
	/**
	 * Returns {@code true} if invokedynamic is supported by the virtual
	 * machine.
	 *
	 * @return {@code true} if it is.
	 * @since 2016/03/15
	 */
	public boolean hasInvokeDynamic()
	{
		return hasinvokedynamic;
	}
	
	/**
	 * Should the new StackMapTable attribute be used when veryifying the byte
	 * code of a class?
	 *
	 * @return {@code true} if the "StackMapTable" attribute should be parsed
	 * instead of "StackMap".
	 * @since 2016/03/20
	 */
	public boolean useStackMapTable()
	{
		return usestackmaptable;
	}
	
	/**
	 * Finds the best matching version with the given ID.
	 *
	 * @param __vid The version ID to get a match for.
	 * @return The matching class version or {@code null} if not found.
	 * @since 2016/03/13
	 */
	public static __ClassVersion__ findVersion(int __vid)
	{
		// Go through all versions, find the best
		__ClassVersion__ best = null;
		for (__ClassVersion__ v : values())
			if (v.version >= __vid)
				if (best == null || v.version > best.version)
					best = v;
		
		// Use the best (if any)
		return best;
	}
}

