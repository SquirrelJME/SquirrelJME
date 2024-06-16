// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * Represents the clutter level that is used.
 *
 * @since 2023/01/28
 */
public enum ClutterLevel
{
	/** Release build. */
	RELEASE,
	
	/** Debug build. */
	DEBUG,
	
	/* End. */
	;
	
	/**
	 * Is this considered a debug task?
	 * 
	 * @return If this is considered debugging.
	 * @since 2023/02/01
	 */
	public final boolean isDebug()
	{
		return this == ClutterLevel.DEBUG;
	}
	
	/**
	 * Returns the opposite clutter level.
	 * 
	 * @return The opposite clutter level.
	 * @since 2023/02/05
	 */
	public final ClutterLevel opposite()
	{
		if (this == ClutterLevel.RELEASE)
			return ClutterLevel.DEBUG;
		return ClutterLevel.RELEASE;
	}
	
	/**
	 * Returns the proper noun of the clutter level.
	 *
	 * @return The proper noun of the clutter level.
	 * @since 2024/03/04
	 */
	public String properNoun()
	{
		if (this == ClutterLevel.RELEASE)
			return "Release";
		return "Debug";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/28
	 */
	@Override
	public String toString()
	{
		if (this == ClutterLevel.RELEASE)
			return "release";
		return "debug";
	}
}
