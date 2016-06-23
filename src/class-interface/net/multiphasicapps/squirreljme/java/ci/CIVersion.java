// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This represents the version of the class.
 *
 * @since 2016/04/24
 */
public enum CIVersion
{
	/** Probably invalid or ancient Java. */
	INVALID(Integer.MIN_VALUE,
		true, false, false, false),	
	
	/** CLDC 1.0 (JSR 30). */
	CLDC_1((45 << 16) + 3,
		false, false, false, false),
	
	/** CLDC 1.1 (JSR 139). */
	CLDC_1_1((47 << 16),
		false, true, false, false),
	
	/** CLDC 8 (aka Java 7). */
	CLDC_8((51 << 16),
		false, true, false, true),
	
	/** Future CLDC version. */
	FUTURE((52 << 16) + 1,
		true, false, false, false),
	
	/** End. */
	;
	
	/** The minimum supported version. */
	public static final CIVersion MIN_VERSION =
		CLDC_1;
	
	/** The maximum supported version. */
	public static final CIVersion MAX_VERSION =
		CLDC_8;
	
	/** The version ID. */
	protected final int version;
	
	/** Undefined? */
	protected final boolean undefined;
	
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
	private CIVersion(int __vid, boolean __undef,
		boolean __float, boolean __hasid, boolean __usesmt)
	{
		// Set
		version = __vid;
		undefined = __undef;
		hasfloat = __float;
		hasinvokedynamic = __hasid;
		usestackmaptable = __usesmt;
	}
	
	/**
	 * Returns {@code true} if floating point is supported by the virtual
	 * machine.
	 *
	 * @return {@code true} if it is.
	 * @throws IllegalStateException If the information this supplies is not
	 * defined.
	 * @since 2016/03/13
	 */
	public boolean hasFloatingPoint()
		throws IllegalStateException
	{
		if (undefined)
			throw new IllegalStateException("AO0c");
		return hasfloat;
	}
	
	/**
	 * Returns {@code true} if invokedynamic is supported by the virtual
	 * machine.
	 *
	 * @return {@code true} if it is.
	 * @throws IllegalStateException If the information this supplies is not
	 * defined.
	 * @since 2016/03/15
	 */
	public boolean hasInvokeDynamic()
		throws IllegalStateException
	{
		if (undefined)
			throw new IllegalStateException("AO0c");
		return hasinvokedynamic;
	}
	
	/**
	 * Should the new StackMapTable attribute be used when veryifying the byte
	 * code of a class?
	 *
	 * @return {@code true} if the StackMapTable attribute should be parsed.
	 * @throws IllegalStateException If the information this supplies is not
	 * defined.
	 * @since 2016/03/20
	 */
	public boolean useStackMapTable()
		throws IllegalStateException
	{
		if (undefined)
			throw new IllegalStateException("AO0c");
		return usestackmaptable;
	}
	
	/**
	 * Finds the best matching version with the given ID.
	 *
	 * @param __vid The version ID to get a match for for.
	 * @return The closest matching version number.
	 * @since 2016/03/13
	 */
	public static CIVersion findVersion(int __vid)
	{
		// Go through all values from last to first
		CIVersion[] vers = values();
		int n = vers.length;
		for (int i = n - 1; i >= 0; i--)
			if (__vid >= vers[i].version)
				return vers[i];
		
		// This really should not occur, but in case it does
		return CIVersion.INVALID;
	}
}

