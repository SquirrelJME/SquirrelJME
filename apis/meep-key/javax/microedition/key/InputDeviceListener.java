// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.key;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/08/30
 */
public interface InputDeviceListener
{
	public abstract void hardwareStateChanged(InputDevice __dev, boolean __hw);

	public abstract void inputDeviceAdded(InputDevice __dev);

	public abstract void inputDeviceRemoved(InputDevice __dev);
}

