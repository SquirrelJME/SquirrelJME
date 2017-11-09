// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.lcdui;

/**
 * This represents the state of the display hardware.
 *
 * @since 2017/10/01
 */
public enum DisplayHardwareState
{
	/** Enabled. */
	ENABLED,
	
	/** Disabled. */
	DISABLED,
	
	/** Removed. */
	ABSENT,
	
	/** End. */
	;
	
	/**
	 * Is this forced to be disabled?
	 *
	 * @return If this is forced to be disabled.
	 * @since 2017/10/01
	 */
	public final boolean forceDisabled()
	{
		return this == DISABLED || this == ABSENT;
	}
}

