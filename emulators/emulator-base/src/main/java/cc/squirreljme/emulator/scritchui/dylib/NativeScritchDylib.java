// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
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
	 * Returns all the fonts which are internally built into the UI
	 * interface.
	 *
	 * @return The internal built-in fonts.
	 * @since 2024/06/12
	 */
	public PencilFontBracket[] builtinFonts()
	{
		// Forward
		return NativeScritchDylib.__builtinFonts(this._stateP);
	}
	
	/**
	 * Returns the component height.
	 *
	 * @param __component The component to get.
	 * @return The height of the given component.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/05/12
	 */
	public int componentHeight(DylibComponentObject __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("NARG");
		
		// Forward
		return NativeScritchDylib.__componentHeight(this._stateP,
			__component.objectP);
	}
	
	/**
	 * Repaints the given component.
	 *
	 * @param __component The component to repaint.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/04/24
	 */
	public void componentRepaint(DylibComponentObject __component,
		int __x, int __y, int __w, int __h)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__componentRepaint(this._stateP,
			__component.objectP, __x, __y, __w, __h);
	}
	
	/**
	 * Revalidates the given component.
	 *
	 * @param __component The component to revalidate.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/21
	 */
	public void componentRevalidate(DylibComponentObject __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments");
		
		NativeScritchDylib.__componentRevalidate(this._stateP,
			__component.objectP);
	}
	
	/**
	 * Sets the input listener for a component.
	 *
	 * @param __component The component to set for.
	 * @param __listener The listener to set.
	 * @throws MLECallError On null arguments or if the listener could not
	 * be set.
	 * @since 2024/06/30
	 */
	public void componentSetInputListener(DylibComponentObject __component,
		ScritchInputListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__componentSetInputListener(this._stateP,
			__component.objectP, __listener);
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
	 * Sets the visibility listener for the component.
	 *
	 * @param __component The component to set.
	 * @param __listener The listener to set.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/06/28
	 */
	public void componentSetVisibleListener(
		DylibComponentObject __component, ScritchVisibleListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		// Forward
		NativeScritchDylib.__componentSetVisibleListener(this._stateP,
			((DylibBaseObject)__component).objectP,
			__listener);
	}
	
	/**
	 * Returns the component width.
	 *
	 * @param __component The component to get.
	 * @return The width of the given component.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/05/12
	 */
	public int componentWidth(DylibComponentObject __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("NARG");
		
		// Forward
		return NativeScritchDylib.__componentWidth(this._stateP,
			__component.objectP);
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
		
		NativeScritchDylib.__containerSetBounds(this._stateP,
			__container.objectPointer(), __component.objectP,
			__x, __y, __w, __h);
	}
	
	/**
	 * Enable focus on a panel.
	 *
	 * @param __panel The panel to modify.
	 * @param __enabled If focus should be enabled or not.
	 * @param __default Should this be the default focus item.
	 * @throws MLECallError On any errors.
	 * @since 2024/04/06
	 */
	public void enableFocus(DylibPanelObject __panel, boolean __enabled,
		boolean __default)
		throws MLECallError
	{
		if (__panel == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__panelEnableFocus(this._stateP,
			__panel.objectP, __enabled, __default);
	}
	
	/**
	 * Derives the given font.
	 *
	 * @param __font The font to derive.
	 * @param __style The new style to select.
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant font.
	 * @throws MLECallError On null arguments, if the style is not valid,
	 * or the pixel size is zero or negative.
	 * @since 2024/06/14
	 */
	public PencilFontBracket fontDerive(DylibPencilFontObject __font,
		int __style, int __pixelSize)
	{
		if (__font == null)
			throw new MLECallError("NARG");
		
		return new DylibPencilFontObject(NativeScritchDylib.__fontDerive(
			this._stateP, __font.objectP, __style, __pixelSize));
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
		
		NativeScritchDylib.__loopExecute(this._stateP, __task);
	}
	
	/**
	 * Executes the given task in the event loop at a later time.
	 *
	 * @param __task The task to run
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/25
	 */
	public void loopExecuteLater(Runnable __task)
		throws MLECallError
	{
		if (__task == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__loopExecuteLater(this._stateP, __task);
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
		
		NativeScritchDylib.__windowContentMinimumSize(this._stateP,
			__window.objectP, __w, __h);
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
	 * @throws MLECallError If the window could not be created.
	 * @since 2024/04/16
	 */
	public ScritchWindowBracket windowNew()
		throws MLECallError
	{
		long windowP = NativeScritchDylib.__windowNew(this._stateP);
		if (windowP == 0)
			throw new MLECallError("Could not create window.");
		
		return new DylibWindowObject(windowP);
	}
	
	/**
	 * Sets the close listener for a window.
	 *
	 * @param __window The window to set the listener for.
	 * @param __listener The listener to call on close.
	 * @throws MLECallError If it could not be set or the window is not valid.
	 * @since 2024/05/13
	 */
	public void windowSetCloseListener(DylibWindowObject __window,
		ScritchCloseListener __listener)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__windowSetCloseListener(this._stateP,
			__window.objectP, __listener);
	}
	
	/**
	 * Sets the visibility of the specified window.
	 *
	 * @param __window The window to set the visibility of.
	 * @param __visible Should the window be visible?
	 * @throws MLECallError On any errors.
	 * @since 2024/04/21
	 */
	public void windowSetVisible(DylibWindowObject __window,
		boolean __visible)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__windowSetVisible(this._stateP, __window.objectP,
			__visible);
	}
	
	/**
	 * Returns all the fonts which are internally built into the UI
	 * interface.
	 *
	 * @return The ScritchUI state pointer.
	 * @since 2024/06/12
	 */
	private static native PencilFontBracket[] __builtinFonts(long __stateP);
	
	/**
	 * Returns the component height. 
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @return The component's height.
	 * @throws MLECallError If it could not be obtained.
	 * @since 2024/05/12
	 */
	private static native int __componentHeight(long __stateP,
		long __componentP)
		throws MLECallError;
	
	/**
	 * Repaints the given component.
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component to repaint.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/04/24
	 */
	private static native void __componentRepaint(long __stateP,
		long __componentP, int __x, int __y, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Revalidates the given component.
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @throws MLECallError On null arguments.
	 * @since 2024/04/21
	 */
	private static native void __componentRevalidate(long __stateP,
		long __componentP)
		throws MLECallError;
	
	/**
	 * Sets the input listener for a given component.
	 *
	 * @param __stateP The current state pointer.
	 * @param __componentP The component to set for.
	 * @param __listener The listener to set.
	 * @throws MLECallError On any errors.
	 * @since 2024/06/30
	 */
	private static native void __componentSetInputListener(long __stateP,
		long __componentP, ScritchInputListener __listener)
		throws MLECallError;
	
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
	 * Sets the visibility listener for a component. 
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @param __listener The listener to set.
	 * @throws MLECallError If the component is not valid or on null
	 * arguments.
	 * @since 2024/06/28
	 */
	private static native void __componentSetVisibleListener(long __stateP,
		long __componentP, ScritchVisibleListener __listener)
		throws MLECallError;
	
	/**
	 * Returns the component width. 
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @return The component's width.
	 * @throws MLECallError If it could not be obtained.
	 * @since 2024/05/12
	 */
	private static native int __componentWidth(long __stateP,
		long __componentP)
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
	 * Sets the bounds of the given component in the container.
	 *
	 * @param __stateP The state pointer.
	 * @param __containerP The container pointer.
	 * @param __componentP The component pointer.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On null arguments or if the bounds are invalid.
	 * @since 2024/04/22
	 */
	private static native void __containerSetBounds(long __stateP,
		long __containerP, long __componentP,
		int __x, int __y, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Derives the given font.
	 *
	 * @param __stateP The state pointer.
	 * @param __fontP The font to derive.
	 * @param __style The new style to select.
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant font pointer.
	 * @throws MLECallError On null arguments, if the style is not valid,
	 * or the pixel size is zero or negative.
	 * @since 2024/06/14
	 */
	private static native long __fontDerive(long __stateP, long __fontP,
		int __style, int __pixelSize)
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
	 * @since 2024/04/16
	 */
	private static native void __loopExecuteLater(long __stateP,
		Runnable __task);
	
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
	 * @param __default Should this be the default focus item?
	 * @throws MLECallError On any errors.
	 * @since 2024/04/06
	 */
	private static native void __panelEnableFocus(long __stateP,
		long __panelP, boolean __enabled, boolean __default)
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
	 * Sets the minimum size for contents in windows.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window pointer.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On null arguments or the width and/or height
	 * are not valid.
	 * @since 2024/04/21
	 */
	private static native void __windowContentMinimumSize(long __stateP,
		long __windowP, int __w, int __h)
		throws MLECallError;
	
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
	
	/**
	 * Sets the close listener for a window.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window to set the listener for.
	 * @param __listener The listener to be called on close.
	 * @throws MLECallError If it could not be set.
	 * @since 2024/05/13
	 */
	private static native void __windowSetCloseListener(long __stateP,
		long __windowP, ScritchCloseListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the visibility of the window.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window to set the visibility of.
	 * @param __visible Should the window be visible?
	 * @throws MLECallError On any errors.
	 * @since 2024/04/21
	 */
	private static native void __windowSetVisible(long __stateP,
		long __windowP, boolean __visible)
		throws MLECallError;
}
