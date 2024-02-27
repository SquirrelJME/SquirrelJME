// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.Path;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

/**
 * Support for native game controllers in the emulated environment, this is
 * mostly for just easing testing.
 *
 * @since 2024/02/26
 */
public class NativeGameController
{
	/** Java input libraries. */
	private static final String[] _JINPUT_LIBS =
		new String[]{"jinput-dx8_64.dll",
		"jinput-raw_64.dll",
		"jinput-wintab.dll",
		"libjinput-linux64.so",
		"libjinput-osx.jnilib"};
	
	/**
	 * {@squirreljme.env cc.squirreljme.gamepad=(device) Selects the gamepad
	 * to use.}.
	 */
	public static final String GAMEPAD_DEVICE =
		"cc.squirreljme.gamepad";
	
	/** The instance of the controller. */
	private static volatile NativeGameController _INSTANCE;
	
	/** The controller to use. */
	protected final Controller controller;
	
	/**
	 * Initializes the native controller support.
	 *
	 * @param __controller The controller to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/26
	 */
	public NativeGameController(Controller __controller)
		throws NullPointerException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		this.controller = __controller;
	}
	
	/**
	 * Poll the game controller and execute key commands.
	 *
	 * @param __callback The callback to send keys to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/26
	 */
	public void pollAll(UIFormCallback __callback)
		throws NullPointerException
	{
		if (__callback == null)
			throw new NullPointerException("NARG");
		
		Controller controller = this.controller;
		
		// Poll controller, if it returns null then the controller is no
		// longer a valid controller (disconnected?)
		if (!controller.poll())
			return;
		
		// Get queue
		EventQueue queue = controller.getEventQueue();
		
		// Process events
		Event event = new Event();
		while (queue.getNextEvent(event))
		{
			Debugging.debugNote("Identifier: %s -> %s",
				event.getComponent().getIdentifier().getClass().getName(),
				event.getComponent().getIdentifier().getName());
		}
	}
	
	/**
	 * Returns the instance of the native game controller.
	 *
	 * @return The native instance or {@code null} if there is nothing.
	 * @since 2024/02/26
	 */
	public static NativeGameController instance()
	{
		// Currently does not work properly
		if (true)
			return null;
		
		// Get instance of the controller
		NativeGameController instance = NativeGameController._INSTANCE;
		if (instance != null)
			return instance;
		
		// Load native input library
		try
		{
			// We need to actually say where the libraries are for this to
			// even work
			for (String lib : NativeGameController._JINPUT_LIBS)
			{
				// Load in resources
				Path path = NativeBinding.libFromResources(lib, false);
				
				// There is no way to tell JInput that it should look here
				if (null == System.getProperty(
					"net.java.games.input.librarypath"))
					System.setProperty("net.java.games.input.librarypath",
						path.getParent().toAbsolutePath().toString());
			}
			
			// Get controller environment
			ControllerEnvironment env =
				ControllerEnvironment.getDefaultEnvironment();
			
			// If not supported, do nothing
			if (!env.isSupported())
				return null;
			
			// There are no controllers?
			Controller[] controllers = env.getControllers();
			if (controllers == null || controllers.length == 0)
				return null;
			
			// Which controller do we want?
			String wantDev =
				System.getProperty(NativeGameController.GAMEPAD_DEVICE);
			
			// Find controller to select
			Controller chosen = null;
			if (wantDev != null)
				for (Controller controller : controllers)
					if (wantDev.equalsIgnoreCase(controller.getName()))
					{
						chosen = controller;
						break;
					}
			
			// Use first controller by default
			if (chosen == null)
				chosen = controllers[0];
			
			// Setup instance
			instance = new NativeGameController(chosen);
			
			// Cache and use it
			NativeGameController._INSTANCE = instance;
			return instance;
		}
		catch (Throwable __t)
		{
			__t.printStackTrace();
			
			return null;
		}
	}
}
