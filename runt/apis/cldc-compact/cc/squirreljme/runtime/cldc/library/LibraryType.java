// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.library;

/**
 * This represents the type of library that this is.
 *
 * @since 2017/12/10
 */
public enum LibraryType
{
	/** An application (MIDlet). */
	APPLICATION,
	
	/** A library (LIBlet). */
	LIBRARY,
	
	/** A system suite. */
	SYSTEM,
	
	/** End. */
	;
}

