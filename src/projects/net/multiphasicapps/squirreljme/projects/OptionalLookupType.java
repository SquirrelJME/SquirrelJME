// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This represents the type of optional lookup to perform.
 *
 * @since 2016/10/11
 */
public enum OptionalLookupType
{
	/** Use no optional dependencies. */
	NONE,
	
	/** Use internal ones only (runs on SquirrelJME). */
	INTERNAL,
	
	/** Use external ones only (runs on Java SE/bootstrap). */
	EXTERNAL,
	
	/** End. */
	;
}

