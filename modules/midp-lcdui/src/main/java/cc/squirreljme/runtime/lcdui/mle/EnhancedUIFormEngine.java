// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a form engine which is used when UI Forms are supported by the
 * native implementation.
 *
 * @since 2020/06/30
 */
public class EnhancedUIFormEngine
	implements UIFormEngine
{
	/**
	 * {@squirreljme.property cc.squirreljme.runtime.lcdui.mle.fallback=bool
	 * Force the usage of the fallback UIForm in the event that native form
	 * handling should NOT be used.}
	 */
	public static final String FORCE_FALLBACK_PROPERTY =
		"cc.squirreljme.runtime.lcdui.mle.fallback";
	
	/**
	 * {@squirreljme.property cc.squirreljme.runtime.lcdui.mle.headless=bool
	 * Force that the headless UIForm be used, this will mean that nothing
	 * will be displayed on the screen.}
	 */
	public static final String FORCE_HEADLESS_PROPERTY =
		"cc.squirreljme.runtime.lcdui.mle.headless";
	
	/** The instance of the form engine to be used. */
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", 
		"NonConstantFieldWithUpperCaseName"})
	private static UIFormEngine _INSTANCE;
	
	/**
	 * Gets an instance of the UI engine.
	 * 
	 * @return The instance of the engine to use.
	 * @since 2020/06/30
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static UIFormEngine getInstance()
	{
		// If this was already cached, use that
		UIFormEngine rv = EnhancedUIFormEngine._INSTANCE;
		if (rv != null)
			return rv;
		
		// Debug
		Debugging.debugNote("Initializing UIFormEngine...");
		
		// These are properties which determine which kind of engine can be
		// returned
		boolean forceFallback = Boolean.getBoolean(
			EnhancedUIFormEngine.FORCE_FALLBACK_PROPERTY);
		boolean forceHeadless = Boolean.getBoolean(
			EnhancedUIFormEngine.FORCE_HEADLESS_PROPERTY);
		boolean isForcing = (forceFallback || forceHeadless);
		
		// Use native forms if supported unless we are forcing other options
		if (0 != UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED) &&
			!isForcing)
			rv = new EnhancedUIFormEngine();
		
		// Otherwise use the fallback implementation (raw framebuffer)
		else
		{
			// Use a headless interface? This is if we have no framebuffer
			// and the only have to have graphics is to fake it
			if (forceHeadless)
				throw Debugging.todo("Implement headless.");
			
			throw Debugging.todo("Implement UIForm fallback.");
		}
		
		// Cache and use
		EnhancedUIFormEngine._INSTANCE = rv;
		return rv;
	}
}
