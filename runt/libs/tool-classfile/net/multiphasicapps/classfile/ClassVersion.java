// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents the class verion that a class may be.
 *
 * @since 2016/06/29
 */
public enum ClassVersion
{
	/** CLDC 1.0 (JSR 30). */
	CLDC_1((45 << 16) + 3, (47 << 16) - 1, false, false, false),
	
	/** CLDC 1.1 (JSR 139). */
	CLDC_1_1((47 << 16), (51 << 16) - 1, true, false, false),
	
	/** CLDC 8 (aka Java 7). */
	CLDC_8((51 << 16), (52 << 16), true, false, true),
	
	/** End. */
	;
	
	/** The minimum supported version. */
	public static final ClassVersion MIN_VERSION =
		CLDC_1;
	
	/** The maximum supported version. */
	public static final ClassVersion MAX_VERSION =
		CLDC_8;
	
	/** The version ID. */
	protected final int version;
	
	/** The maximum range of the version. */
	protected final int maxversion;
	
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
	 * @param __vmx The max version identifier.
	 * @param __undef Is this version information undefined?
	 * @param __float Is floating point supported?
	 * @param __hasid Has invoke dynamic support?
	 * @param __usesmt Should the StackMapTable attribute be used?
	 * @since 2016/03/13
	 */
	private ClassVersion(int __vid, int __vmx,
		boolean __float, boolean __hasid, boolean __usesmt)
	{
		// Set
		version = __vid;
		maxversion = __vmx;
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
	public static ClassVersion findVersion(int __vid)
	{
		// Go through all versions, find the best
		ClassVersion best = null;
		for (ClassVersion v : values())
			if (__vid >= v.version && __vid <= v.maxversion)
				if (best == null || v.version > best.version)
					best = v;
		
		// Use the best (if any)
		return best;
	}
}

