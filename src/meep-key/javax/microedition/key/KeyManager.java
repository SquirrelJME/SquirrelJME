// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.key;

import java.util.Iterator;

public class KeyManager
{
	/**
	 * Not used.
	 *
	 * @since 2016/08/30
	 */
	private KeyManager()
	{
	}
	
	public static Iterator<InputDevice> getInputDevices()
	{
		throw new Error("TODO");
	}

	public static Iterator<InputDevice> getHeadlessInputDevices()
	{
		throw new Error("TODO");
	}

	public static void addInputDeviceListener(InputDeviceListener __dl)
	{
		throw new Error("TODO");
	}

	public static void removeInputDeviceListener(InputDeviceListener __dl)
	{
		throw new Error("TODO");
	}

	public static void setGeneralKeyListener(KeyListener __kl)
	{
		throw new Error("TODO");
	}
}

