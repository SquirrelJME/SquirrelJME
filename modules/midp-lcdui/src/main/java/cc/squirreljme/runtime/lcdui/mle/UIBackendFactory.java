// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.fb.FBUIBackend;
import cc.squirreljme.runtime.lcdui.mle.fb.NativeFBAttachment;
import cc.squirreljme.runtime.lcdui.mle.fb.UIFormAttachment;
import cc.squirreljme.runtime.lcdui.mle.headless.HeadlessAttachment;
import cc.squirreljme.runtime.lcdui.mle.pure.NativeUIBackend;

/**
 * Used to gain access to the {@link UIBackend}.
 *
 * @since 2020/07/19
 */
public final class UIBackendFactory
{
	/**
	 * {@squirreljme.property cc.squirreljme.runtime.lcdui.mle.backend=type
	 * Force a specific backend to be used, may be one of: native, canvas,
	 * framebuffer, or headless.}
	 */
	public static final String FORCE_BACKEND_PROPERTY =
		"cc.squirreljme.runtime.lcdui.mle.backend";
	
	/** The default form type. */
	private static volatile UIBackendType _defaultType;
	
	/** The current backend. */
	private static volatile UIBackend _currentBackend;
	
	/**
	 * Returns the default backend type.
	 * 
	 * @return The default type.
	 * @since 2022/07/21
	 */
	public static UIBackendType defaultType()
	{
		synchronized (UIBackendFactory.class)
		{
			// Was one already specified?
			UIBackendType defaultType = UIBackendFactory._defaultType;
			if (defaultType != null)
				return defaultType;
			
			// Is one being forced?
			String forced = System.getProperty(
				UIBackendFactory.FORCE_BACKEND_PROPERTY);
			if (forced != null)
				try
				{
					UIBackendType maybe =
						UIBackendType.valueOf(forced.trim().toUpperCase());
					if (UIBackendFactory.isSupported(maybe))
						return maybe;
				}
				catch (IllegalArgumentException ignored)
				{
				}
			
			// Go through all and get the first one supported
			for (UIBackendType type : UIBackendType.values())
				if (UIBackendFactory.isSupported(type))
					return type;
		}
		
		// Should not be reached
		throw Debugging.oops();
	}
	
	/**
	 * Gets an instance of the UI engine.
	 * 
	 * @return The instance of the engine to use.
	 * @since 2020/06/30
	 */
	public static UIBackend getInstance()
	{
		synchronized (UIBackendFactory.class)
		{
			// If this was already cached, use that
			UIBackend rv = UIBackendFactory._currentBackend;
			if (rv != null)
				return rv;
			
			// Get the default to use, make sure it sticks
			UIBackendType type = UIBackendFactory.defaultType();
			UIBackendFactory._defaultType = type;
			
			// Debug
			Debugging.debugNote("Initializing UIFormEngine...");
			
			// Depends on the type
			switch (type)
			{
					// Native forms
				case NATIVE:
					rv = new NativeUIBackend();
					break;
					
					// Attached to just a canvas
				case CANVAS:
					rv = new FBUIBackend(new UIFormAttachment());
					break;
					
					// Attached to regular framebuffer
				case FRAMEBUFFER:
					rv = new FBUIBackend(new NativeFBAttachment());
					break;
					
					// Headless, no display
				case HEADLESS:
					rv = new FBUIBackend(new HeadlessAttachment(
						UIPixelFormat.INT_RGB888, 240, 320));
					break;
				
				default:
					throw Debugging.oops();
			}
			
			// Cache and use
			UIBackendFactory._currentBackend = rv;
			return rv;
		}
	}
	
	/**
	 * Checks if the given backend is supported.
	 * 
	 * @param __type The type to check.
	 * @return If this is supported or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/21
	 */
	public static boolean isSupported(UIBackendType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		switch (__type)
		{
				// Native UI Forms
			case NATIVE:
				if (0 == UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED))
					return false;
				
				// If in canvas only mode, then do not use this
				return 0 == UIFormShelf.metric(
					UIMetricType.CANVAS_ONLY_SUPPORT);
				
				// Only on Native UI Canvas
			case CANVAS:
				return 0 != UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED);
				
				// Is the framebuffer supported?
			case FRAMEBUFFER:
				Debugging.todoNote("Support native framebuffer.");
				return false;
				
				// Headless is always supported
			case HEADLESS:
				return true;
			
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Sets the default backend factory to use.
	 * 
	 * @param __type The type to use.
	 * @throws IllegalStateException If one was already set.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/21
	 */
	public static void setDefault(UIBackendType __type)
		throws IllegalStateException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		synchronized (UIBackendFactory.class)
		{
			// {@squirreljme.error EB3a Default has already been set.}
			if (UIBackendFactory._defaultType != null)
				throw new IllegalStateException("EB3a");
			
			// Set it now
			UIBackendFactory._defaultType = __type;
		}
	}
}
