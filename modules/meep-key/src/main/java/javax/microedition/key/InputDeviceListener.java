// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
	void hardwareStateChanged(InputDevice __dev, boolean __hw);

	void inputDeviceAdded(InputDevice __dev);

	void inputDeviceRemoved(InputDevice __dev);
}

