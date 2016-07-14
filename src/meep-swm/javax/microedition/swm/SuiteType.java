// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

