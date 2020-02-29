// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

/**
 * Represents the type of MIDlet a program is.
 *
 * @since 2020/02/15
 */
public enum JavaMEMidletType
{
	/** API. */
	API,
	
	/** Library. */
	LIBRARY,
	
	/** Application. */
	APPLICATION,
	
	/* End. */
	;
	
	/**
	 * Returns the dependency key.
	 *
	 * @param __i The dependency index.
	 * @return The dependency key.
	 * @since 2020/02/28
	 */
	public final String dependencyKey(int __i)
	{
		return this.prefixKey() + "-Dependency-" + __i;
	}
	
	/**
	 * Returns the name key.
	 *
	 * @return The name key.
	 * @since 2020/02/28
	 */
	public final String nameKey()
	{
		return this.prefixKey() + "-Name";
	}
	
	/**
	 * Returns the prefix key.
	 *
	 * @return The prefix key.
	 * @since 2020/02/28
	 */
	public final String prefixKey()
	{
		switch (this)
		{
			case API:			return "X-SquirrelJME-API";
			case LIBRARY:		return "LIBlet";
			case APPLICATION:	return "MIDlet";
			
			default:
				throw new RuntimeException("Unknown project type.");
		}
	}
	
	/**
	 * Returns the vendor key.
	 *
	 * @return The vendor key.
	 * @since 2020/02/28
	 */
	public final String vendorKey()
	{
		return this.prefixKey() + "-Vendor";
	}
	
	/**
	 * Returns the version key.
	 *
	 * @return The version key.
	 * @since 2020/02/28
	 */
	public final String versionKey()
	{
		return this.prefixKey() + "-Version";
	}
}

