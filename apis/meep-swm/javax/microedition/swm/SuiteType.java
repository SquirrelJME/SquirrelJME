// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * The type that a suite is.
 *
 * @since 2016/06/24
 */
public enum SuiteType
{
	/** An application (MIDlet). */
	APPLICATION,
	
	/** Invalid. */
	INVALID,
	
	/** A library (LIBlet). */
	LIBRARY,
	
	/** A system suite. */
	SYSTEM,
	
	/** End. */
	;
}

