// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import java.nio.file.Path;

/**
 * Native dynamic library that directly wraps the C-based ScritchUI API.
 *
 * @since 2024/03/29
 */
public final class NativeScritchDylib
{
	/** The state pointer. */
	private final long _stateP;
	
	/**
	 * Initializes the native library layer for ScritchUI.
	 *
	 * @param __libPath The library path to load.
	 * @param __name The name of the ScritchUI interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/30
	 */
	public NativeScritchDylib(Path __libPath, String __name)
		throws NullPointerException
	{
		if (__libPath == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Link in native library and locate the structure
		long stateP = NativeScritchDylib.__linkInit(
			__libPath.toAbsolutePath().toString(),
			__name.toLowerCase());
		if (stateP == 0)
			throw new MLECallError(String.format(
				"Could not initialize ScritchUI library '%s' (%s)",
				__libPath, __name));
		this._stateP = stateP;
	}
	
	/**
	 * Sets the component paint listener.
	 *
	 * @param __component The component to draw on.
	 * @param __listener The listener to use.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/06
	 */
	public void componentSetPaintListener(DylibPaintableObject __component,
		ScritchPaintListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("NARG");
		
		// Forward
		NativeScritchDylib.__componentSetPaintListener(this._stateP,
			((DylibBaseObject)__component).objectP,
			__listener);
	}
	
	/**
	 * Enable focus on a panel.
	 *
	 * @param __panel The panel to modify.
	 * @param __enabled If focus should be enabled or not.
	 * @throws MLECallError On any errors.
	 * @since 2024/04/06
	 */
	public void enableFocus(DylibPanelObject __panel, boolean __enabled)
		throws MLECallError
	{
		if (__panel == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__panelEnableFocus(this._stateP,
			__panel.objectP, __enabled);
	}
	
	/**
	 * Initializes a new panel.
	 *
	 * @return The newly created panel.
	 * @throws MLECallError If the panel could not be created.
	 * @since 2024/04/06
	 */
	public DylibPanelObject panelNew()
		throws MLECallError
	{
		long panelP = NativeScritchDylib.__panelNew(this._stateP);
		if (panelP == 0)
			throw new MLECallError("Could not create panel.");
		
		return new DylibPanelObject(panelP);
	}
	
	/**
	 * Returns the available screens.
	 *
	 * @return The available screens.
	 * @since 2024/04/06
	 */
	public ScritchScreenBracket[] screens()
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Sets the paint listener for the component.
	 *
	 * @param __stateP The state used.
	 * @param __componentP The object pointer.
	 * @param __listener The listener to use.
	 * @throws MLECallError On any errors.
	 * @since 2024/04/06
	 */
	private static native void __componentSetPaintListener(long __stateP,
		long __componentP, ScritchPaintListener __listener)
		throws MLECallError;
	
	/**
	 * Link in the library and load the given structure pointer.
	 *
	 * @param __libPath The library path.
	 * @param __name The interface name.
	 * @return The resultant ScritchUI state pointer.
	 * @since 2024/03/31
	 */
	private static native long __linkInit(String __libPath, String __name);
	
	/**
	 * Enables or disables a panel being focusable. 
	 *
	 * @param __stateP The current state pointer.
	 * @param __panelP The component pointer.
	 * @param __enabled Should focus be enabled?
	 * @throws MLECallError On any errors.
	 * @since 2024/04/06
	 */
	private static native void __panelEnableFocus(long __stateP,
		long __panelP, boolean __enabled)
		throws MLECallError;
	
	/**
	 * Initializes a new panel. 
	 *
	 * @param __stateP The state pointer.
	 * @return The pointer to the panel.
	 * @since 2024/04/06
	 */
	private static native long __panelNew(long __stateP);
}
