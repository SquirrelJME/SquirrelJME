// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui;

/**
 * This interface is used for factories that create display services that
 * is used by the SquirrelJME MEEP LUI code to provide an interface to the
 * user.
 *
 * @since 2016/09/07
 */
public interface DisplayServiceFactory
{
	/**
	 * Attempts to create the display service and then returns it.
	 *
	 * @return The display service.
	 * @throws InvalidDisplayServiceException If the display service cannot
	 * be used.
	 * @since 2016/09/07
	 */
	public abstract DisplayService createDisplayService()
		throws InvalidDisplayServiceException;
	
	/**
	 * Returns the service priority.
	 *
	 * @return The prority of the driver, the service lookup will auto-select
	 * the driver with the highest number. A negative value means that it
	 * will never be considered for auto-selection.
	 * @since 2016/09/07
	 */
	public abstract int priority();
}

