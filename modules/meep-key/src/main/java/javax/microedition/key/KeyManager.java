// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
		throw new todo.TODO();
	}

	public static Iterator<InputDevice> getHeadlessInputDevices()
	{
		throw new todo.TODO();
	}

	public static void addInputDeviceListener(InputDeviceListener __dl)
	{
		throw new todo.TODO();
	}

	public static void removeInputDeviceListener(InputDeviceListener __dl)
	{
		throw new todo.TODO();
	}

	public static void setGeneralKeyListener(KeyListener __kl)
	{
		throw new todo.TODO();
	}
}

