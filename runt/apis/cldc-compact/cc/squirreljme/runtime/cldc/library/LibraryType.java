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
@Deprecated
public enum LibraryType
{
	/** An application (MIDlet). */
	@Deprecated
	APPLICATION,
	
	/** A library (LIBlet). */
	@Deprecated
	LIBRARY,
	
	/** A system suite. */
	@Deprecated
	SYSTEM,
	
	/** End. */
	;
}

