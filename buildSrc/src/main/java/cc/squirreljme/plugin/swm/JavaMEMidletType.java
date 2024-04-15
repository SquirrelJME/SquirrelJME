// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

import cc.squirreljme.plugin.multivm.VMHelpers;
import org.gradle.api.tasks.SourceSet;

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
	 * Normalizes the type of MIDlet or otherwise this is based on the given
	 * context of the source set.
	 * 
	 * @param __sourceSet The source set to get from.
	 * @return The normalized MIDlet type.
	 * @since 202/08/07
	 */
	public final JavaMEMidletType normalizeViaSourceSet(String __sourceSet)
	{
		switch (__sourceSet)
		{
				// Uses whatever this is
			case SourceSet.MAIN_SOURCE_SET_NAME:
				return this;
				
				// Always an application
			case SourceSet.TEST_SOURCE_SET_NAME:
				return JavaMEMidletType.APPLICATION;
				
				// Always a library
			case VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME:
				return JavaMEMidletType.LIBRARY;
			
			default:
				throw new IllegalArgumentException("Unknown source set: " +
					__sourceSet);
		}
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

