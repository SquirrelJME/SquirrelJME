// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This represents the type of operating system SquirrelJME is running on.
 *
 * This enumeration is intended to make OS detection standard.
 *
 * @since 2018/01/13
 */
public enum OperatingSystemType
{
	/** Linux. */
	LINUX,
	
	/** Windows. */
	WINDOWS,
	
	/** End. */
	;
	
	/**
	 * Is this a UNIX system?
	 *
	 * @return {@code true} if it is a UNIX system.
	 * @since 2018/01/13
	 */
	public final boolean isUnix()
	{
		switch (this)
		{
			case LINUX:
				return true;
			
			default:
				return false;
		}
	}
}

