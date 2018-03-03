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

import cc.squirreljme.runtime.cldc.library.LibraryControlKey;

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
	
	/**
	 * Returns the control key for the given type.
	 *
	 * @return The control key to obtain.
	 * @since 2018/03/03
	 */
	final LibraryControlKey __controlKey()
	{
		switch (this)
		{
			case AVAILABLE:
				return LibraryControlKey.STATE_FLAG_AVAILABLE;
			
			case ENABLED:
				return LibraryControlKey.STATE_FLAG_ENABLED;
			
			case PREINSTALLED:
				return LibraryControlKey.STATE_FLAG_PREINSTALLED;
			
			case REMOVE_DENIED:
				return LibraryControlKey.STATE_FLAG_REMOVE_DENIED;
			
			case SYSTEM:
				return LibraryControlKey.STATE_FLAG_SYSTEM;
			
			case UPDATE_DENIED:
				return LibraryControlKey.STATE_FLAG_UPDATE_DENIED;
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

