// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.key;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;

@Api
@SuppressWarnings("ClassWithOnlyPrivateConstructors")
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
	
	@Api
	public static Iterator<InputDevice> getInputDevices()
	{
		throw Debugging.todo();
	}

	@Api
	public static Iterator<InputDevice> getHeadlessInputDevices()
	{
		throw Debugging.todo();
	}

	@Api
	public static void addInputDeviceListener(InputDeviceListener __dl)
	{
		throw Debugging.todo();
	}

	@Api
	public static void removeInputDeviceListener(InputDeviceListener __dl)
	{
		throw Debugging.todo();
	}

	@Api
	public static void setGeneralKeyListener(KeyListener __kl)
	{
		throw Debugging.todo();
	}
}

