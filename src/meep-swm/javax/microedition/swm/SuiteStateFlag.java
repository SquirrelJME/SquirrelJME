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
 * This represents a flag which may be associated with a suite.
 *
 * @since 2016/06/24
 */
public enum SuiteStateFlag
{
	/** Available. */
	AVAILABLE,
	
	/** Enabled, the application or library may be used. */
	ENABLED,
	
	/** The application or library is pre-installed with the system. */
	PREINSTALLED,
	
	/** Remove is not supported. */
	REMOVE_DENIED,
	
	/** A suite provided by the system. */
	SYSTEM,
	
	/** The suite cannot be updated. */
	UPDATE_DENIED,
	
	/** End. */
	;
}

