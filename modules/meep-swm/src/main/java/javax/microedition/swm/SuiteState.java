// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This represents the state that a suite is in.
 *
 * @since 2016/06/24
 */
public enum SuiteState
{
	/** Installation failed. */
	INSTALLATION_FAILED,
	
	/** Installed. */
	INSTALLED,
	
	/** Currently being installed. */
	INSTALLING,
	
	/** Removed. */
	REMOVED,
	
	/** End. */
	;
}

