// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is a listener which is associated with the {@link Display} class and
 * is called in the event that new displays are added or removed from a
 * device.
 *
 * @since 2016/08/30
 */
@Api
public interface DisplayListener
{
	/**
	 * This is called when a new display has been added to the device.
	 *
	 * @param __d The display which was added.
	 * @since 2016/08/30
	 */
	@Api
	void displayAdded(Display __d);
	
	/**
	 * This is called when a display has been removed from the device, any
	 * actions on the display following this might not have any effect.
	 *
	 * @param __d The display which was removed.
	 * @since 2016/08/30
	 */
	@Api
	void displayRemoved(Display __d);
	
	/**
	 * This is called when the state of a display changes in hardware.
	 *
	 * @param __d The display that changed state.
	 * @param __ns If {@code true} then the display was reassigned on the
	 * underlying hardware, if {@code false} then the display was unassigned.
	 * @since 2016/08/30
	 */
	@Api
	void hardwareStateChanged(Display __d, boolean __ns);
}

