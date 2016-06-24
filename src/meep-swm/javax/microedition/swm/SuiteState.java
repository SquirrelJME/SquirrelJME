// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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

