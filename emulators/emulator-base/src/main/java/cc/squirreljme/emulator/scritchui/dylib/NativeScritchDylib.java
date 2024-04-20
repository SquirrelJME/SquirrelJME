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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchWindowManagerType;
import java.nio.file.Path;

/**
 * Native dynamic library that directly wraps the C-based ScritchUI API.
 *
 * @since 2024/03/29
 */
public final class NativeScritchDylib
{
	/** Default number of screens to request. */
	private static final int _REQUEST_SCREENS =
		16;
	
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
	 * Adds component to the container.
	 *
	 * @param __container The container to add to.
	 * @param __component The component to add.
	 * @throws MLECallError On null arguments or the add is not valid.
	 * @since 2024/04/18
	 */
	public void containerAdd(DylibContainerObject __container,
		DylibComponentObject __component)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__containerAdd(this._stateP,
			__container.objectPointer(), __component.objectP);
	}
	
	/**
	 * Removes all components from the container.
	 *
	 * @param __container The container to remove from.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/18
	 */
	public void containerRemoveAll(DylibContainerObject __container)
		throws MLECallError
	{
		if (__container == null)
			throw new MLECallError("Null arguments.");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Sets the bounds for the component in the container.
	 *
	 * @param __container The container to set within.
	 * @param __component The component to set the bounds of.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On null arguments or the bounds set is not valid.
	 * @since 2024/04/18
	 */
	public void containerSetBounds(DylibContainerObject __container,
		DylibComponentObject __component, int __x, int __y, int __w, int __h)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("Null arguments.");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
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
	 * Executes the given task in the event loop or the current thread if
	 * this is the event loop.
	 *
	 * @param __task The task to run
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/16
	 */
	public void loopExecute(Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("NARG");
		
		if (this.loopIsInThread())
			__task.run();
		else
			NativeScritchDylib.__loopExecute(this._stateP, __task);
	}
	
	/**
	 * Executes the given task in the event loop or the current thread if
	 * this is the event loop.
	 *
	 * @param __task The task to run
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/17
	 */
	public void loopExecuteWait(Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("NARG");
		
		if (this.loopIsInThread())
			__task.run();
		else
			NativeScritchDylib.__loopExecuteWait(this._stateP, __task);
	}
	
	/**
	 * Is this in the event loop?
	 *
	 * @return If this is in the event loop.
	 * @since 2024/04/16
	 */
	public boolean loopIsInThread()
	{
		return NativeScritchDylib.__loopIsInThread(this._stateP);
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
		// Read in screens
		int numScreens = NativeScritchDylib._REQUEST_SCREENS;
		for (;;)
		{
			// Request all screens
			long[] screenPs = new long[numScreens];
			numScreens = NativeScritchDylib.__screens(this._stateP, screenPs);
			
			// Not big enough?
			if (numScreens > screenPs.length)
				continue;
			
			// Map them to objects
			ScritchScreenBracket[] result =
				new ScritchScreenBracket[numScreens];
			for (int i = 0; i < numScreens; i++)
				result[i] = new DylibScreenObject(screenPs[i]);
			
			return result;
		}
	}
	
	/**
	 * Sets the minimum size to use for a window's contents.
	 *
	 * @param __window The window to set the content size of.
	 * @param __w The width
	 * @param __h The height.
	 * @throws MLECallError On null arguments or the size is not valid.
	 * @since 2024/04/20
	 */
	public void windowContentMinimumSize(DylibWindowObject __window,
		int __w, int __h)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments");
		if (__w <= 0 || __h <= 0)
			throw new MLECallError("Zero or negative size");
			
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Returns the {@link ScritchWindowManagerType}.
	 *
	 * @return A {@link ScritchWindowManagerType}.
	 * @since 2024/04/15
	 */
	public int windowManagerType()
	{
		return NativeScritchDylib.__windowManagerType(this._stateP);
	}
	
	/**
	 * Creates a new window.
	 *
	 * @return The newly created window.
	 * @since 2024/04/16
	 */
	public ScritchWindowBracket windowNew()
	{
		long windowP = NativeScritchDylib.__windowNew(this._stateP);
		if (windowP == 0)
			throw new MLECallError("Could not create window.");
		
		return new DylibWindowObject(windowP);
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
	 * Adds the given component to the given container.
	 *
	 * @param __stateP The state pointer.
	 * @param __containerP The container pointer.
	 * @param __componentP The component pointer.
	 * @throws MLECallError On null arguments or the container and/or component
	 * are not valid.
	 * @since 2024/04/20
	 */
	private static native void __containerAdd(long __stateP,
		long __containerP, long __componentP)
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
	 * Executes the given runnable in the loop.
	 *
	 * @param __stateP The state pointer.
	 * @param __task The task to run.
	 * @since 2024/04/16
	 */
	private static native void __loopExecute(long __stateP, Runnable __task);
	
	/**
	 * Executes the given runnable in the loop.
	 *
	 * @param __stateP The state pointer.
	 * @param __task The task to run.
	 * @since 2024/04/17
	 */
	private static native void __loopExecuteWait(long __stateP,
		Runnable __task);
	
	/**
	 * Is this thread in the event loop?
	 *
	 * @param __stateP The state pointer.
	 * @return If this is in the event loop or not.
	 * @since 2024/04/16
	 */
	private static native boolean __loopIsInThread(long __stateP);
	
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
	
	/**
	 * Queries the native screens.
	 *
	 * @param __stateP The state pointer.
	 * @param __screenPs The resultant screen pointers.
	 * @return The number of screens returned, may be higher than the input
	 * if there are more screens.
	 * @since 2024/04/15
	 */
	private static native int __screens(long __stateP, long[] __screenPs);
	
	/**
	 * Returns the {@link ScritchWindowManagerType}.
	 *
	 * @param __stateP The state pointer.
	 * @return A {@link ScritchWindowManagerType}.
	 * @since 2024/04/15
	 */
	private static native int __windowManagerType(long __stateP);
	
	/**
	 * Creates a new window.
	 *
	 * @param __stateP The state pointer.
	 * @return The newly created window or {@code 0} if it could not be
	 * created.
	 * @since 2024/04/16
	 */
	private static native long __windowNew(long __stateP);
}
