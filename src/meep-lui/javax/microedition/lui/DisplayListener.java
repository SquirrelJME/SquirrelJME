// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

/**
 * This is a listener which is associated with the {@link Display} class and
 * is called in the event that new displays are added or removed from a
 * device.
 *
 * @since 2016/08/30
 */
public interface DisplayListener
{
	/**
	 * This is called when a new display has been added to the device.
	 *
	 * @param __d The display which was added.
	 * @since 2016/08/30
	 */
	public abstract void displayAdded(Display __d);
	
	/**
	 * This is called when a display has been removed from the device, any
	 * actions on the display following this might not have any effect.
	 *
	 * @param __d The display which was removed.
	 * @since 2016/08/30
	 */
	public abstract void displayRemoved(Display __d);
	
	/**
	 * This is called when the state of a display changes in hardware.
	 *
	 * @param __ns If {@code true} then the display was reassigned on the
	 * underlying hardware, if {@code false} then the display was unassigned.
	 * @since 2016/08/30
	 */
	public abstract void hardwareStateChanged(Display __d, boolean __ns);
}

