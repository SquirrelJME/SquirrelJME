// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

/**
 * This interface bitflags which represents the type of a program.
 *
 * @since 2017/12/10
 */
public interface LibraryType
{
	/** An application (MIDlet). */
	public static final int APPLICATION =
		0x0000_0001;
	
	/** A library (LIBlet). */
	public static final int LIBRARY =
		0x0000_0002;
	
	/** A system suite. */
	public static final int SYSTEM =
		0x0000_0004;
}

