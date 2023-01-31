// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * The type that a suite is.
 *
 * @since 2016/06/24
 */
@Api
public enum SuiteType
{
	/** An application (MIDlet). */
	@Api
	APPLICATION,
	
	/** Invalid. */
	@Api
	INVALID,
	
	/** A library (LIBlet). */
	@Api
	LIBRARY,
	
	/** A system suite. */
	@Api
	SYSTEM,
	
	/** End. */
	;
}

