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
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a form engine which is used when UI Forms are supported by the
 * native implementation.
 *
 * @since 2020/06/30
 */
public class NativeUIBackend
	implements UIBackend
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
	private static UIBackend _INSTANCE;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public UIDisplayInstance[] displays()
	{
		UIDisplayBracket[] natural = UIFormShelf.displays();
		
		int n = natural.length;
		UIDisplayInstance[] rv = new UIDisplayInstance[n];
		for (int i = 0; i < n; i++)
			rv[i] = new NativeUIDisplayInstance(natural[i]);
		 
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public void formDelete(UIFormInstance __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		// Natively delete the form
		UIFormShelf.formDelete(NativeUIBackend.__native(__form));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public UIFormInstance formNew()
	{
		return new NativeUIFormInstance(UIFormShelf.formNew());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public void displayShow(UIDisplayInstance __display, UIFormInstance __form)
		throws NullPointerException
	{
		if (__display == null || __form == null)
			throw new NullPointerException("NARG");
		
		UIFormShelf.displayShow(NativeUIBackend.__native(__display),
			NativeUIBackend.__native(__form));
	}
	
	/**
	 * Gets an instance of the UI engine.
	 * 
	 * @return The instance of the engine to use.
	 * @since 2020/06/30
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static UIBackend getInstance()
	{
		// If this was already cached, use that
		UIBackend rv = NativeUIBackend._INSTANCE;
		if (rv != null)
			return rv;
		
		// Debug
		Debugging.debugNote("Initializing UIFormEngine...");
		
		// These are properties which determine which kind of engine can be
		// returned
		boolean forceFallback = Boolean.getBoolean(
			NativeUIBackend.FORCE_FALLBACK_PROPERTY);
		boolean forceHeadless = Boolean.getBoolean(
			NativeUIBackend.FORCE_HEADLESS_PROPERTY);
		boolean isForcing = (forceFallback || forceHeadless);
		
		// Use native forms if supported unless we are forcing other options
		if (0 != UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED) &&
			!isForcing)
			rv = new NativeUIBackend();
		
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
		NativeUIBackend._INSTANCE = rv;
		return rv;
	}
	
	/**
	 * Returns the native bracket.
	 * 
	 * @param __instance The instance.
	 * @return The native bracket.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	static UIDisplayBracket __native(UIDisplayInstance __instance)
		throws NullPointerException
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		return ((NativeUIDisplayInstance)__instance).display;
	}
	
	/**
	 * Returns the native bracket.
	 * 
	 * @param __instance The instance.
	 * @return The native bracket.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	static UIFormBracket __native(UIFormInstance __instance)
		throws NullPointerException
	{
		if (__instance == null)
			throw new NullPointerException("NARG");
		
		return ((NativeUIFormInstance)__instance).form;
	}
}
