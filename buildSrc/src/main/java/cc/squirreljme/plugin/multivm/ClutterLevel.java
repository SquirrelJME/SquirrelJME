// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
