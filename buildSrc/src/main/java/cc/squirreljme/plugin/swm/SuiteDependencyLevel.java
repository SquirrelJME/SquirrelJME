// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

/**
 * This represents the level of the dependency.
 *
 * @since 2017/02/22
 */
public enum SuiteDependencyLevel
{
	/** Required. */
	REQUIRED,
	
	/** Optional. */
	OPTIONAL,
	
	/** End. */
	;
	
	/**
	 * Is this an optional dependency level?
	 *
	 * @return {@code true} if this is an optional dependency level.
	 * @since 2017/11/22
	 */
	public boolean isOptional()
	{
		return this == SuiteDependencyLevel.OPTIONAL;
	}
	
	/**
	 * Is this an required dependency level?
	 *
	 * @return {@code true} if this is an required dependency level.
	 * @since 2017/11/22
	 */
	public boolean isRequired()
	{
		return this == SuiteDependencyLevel.REQUIRED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		// Convert string
		switch (this)
		{
			case REQUIRED:	return "required";
			case OPTIONAL:	return "optional";
			default:
				throw new RuntimeException(this.toString());
		}
	}
	
	/**
	 * Returns the dependency level based on the input string.
	 *
	 * @param __s The input string to parse.
	 * @return The dependency level for the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public static SuiteDependencyLevel of(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s.trim())
		{
			case "required":	return SuiteDependencyLevel.REQUIRED;
			case "optional":	return SuiteDependencyLevel.OPTIONAL;
			
				// Should not happen
			default:
				throw new RuntimeException(__s);
		}
	}
}

