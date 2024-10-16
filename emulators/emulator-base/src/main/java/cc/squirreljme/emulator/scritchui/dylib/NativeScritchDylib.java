// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchCloseListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchMenuItemActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeSuggestListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchViewListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchWindowManagerType;
import java.nio.file.Path;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Native dynamic library that directly wraps the C-based ScritchUI API.
 *
 * @since 2024/03/29
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public final class NativeScritchDylib
{ 
	/** The state pointer. */
	final long _stateP;
	
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
	 * @return The ScritchUI state pointer.
	 * @since 2024/06/12
	 */
	static native PencilFontBracket[] __builtinFonts(long __stateP);
	
	/**
	 * Returns the first selected index of the given choice.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice pointer.
	 * @return The resultant index or {@code -1} if there is none.
	 * @throws MLECallError If the state and/or choice are not valid.
	 * @since 2024/07/28
	 */
	static native int __choiceGetSelectedIndex(long __stateP, long __choiceP)
		throws MLECallError;
	
	/**
	 * Inserts the item at the given index.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to insert at.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native int __choiceInsert(long __stateP, long __choiceP,
		int __atIndex)
		throws MLECallError;
	
	/**
	 * Returns the length of this choice.
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @return The length of this choice.
	 * @throws MLECallError The state and/or component pointer are not valid.
	 * @since 2024/07/28
	 */
	static native int __choiceLength(long __stateP, long __componentP)
		throws MLECallError;
	
	/**
	 * Deletes the item at the given index.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to delete.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceRemove(long __stateP, long __choiceP,
		int __atIndex)
		throws MLECallError;
	
	/**
	 * Deletes all items from the choice.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @throws MLECallError On null arguments; or the choice is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceRemoveAll(long __stateP, long __choiceP)
		throws MLECallError;
	
	/**
	 * Sets whether the given index is enabled.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __enabled If this is to be enabled or not.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceSetEnabled(long __stateP, long __choiceP,
		int __atIndex, boolean __enabled)
		throws MLECallError;
	
	/**
	 * Sets the image for the given index.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __data The image data.
	 * @param __off The offset into the data.
	 * @param __scanLen The scanline length.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceSetImage(long __stateP, long __choiceP,
		int __atIndex, int[] __data, int __off, int __scanLen,
		int __width, int __height)
		throws MLECallError;
	
	/**
	 * Sets whether the given item is selected.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __selected If this is to be selected or not.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceSetSelected(long __stateP, long __choiceP,
		int __atIndex, boolean __selected)
		throws MLECallError;
	
	/**
	 * Sets the string for the given index.
	 *
	 * @param __stateP The state pointer.
	 * @param __choiceP The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __string The string to set for the item.
	 * @throws MLECallError On null arguments; the choice is not valid; or
	 * the index is not valid.
	 * @since 2024/07/25
	 */
	static native void __choiceSetString(long __stateP, long __choiceP,
		int __atIndex, String __string)
		throws MLECallError;
	
	/**
	 * Gets the parent of the given component.
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @return The parent of this component or {@code null} if it has none.
	 * @throws MLECallError If the state and/or component are not valid.
	 * @since 2024/07/29
	 */
	static native ScritchComponentBracket __componentGetParent(long __stateP,
		long __componentP)
		throws MLECallError;
	
	/**
	 * Returns the component height. 
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @return The component's height.
	 * @throws MLECallError If it could not be obtained.
	 * @since 2024/05/12
	 */
	static native int __componentHeight(long __stateP,
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
	static native void __componentRepaint(long __stateP,
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
	static native void __componentRevalidate(long __stateP,
		long __componentP)
		throws MLECallError;
	
	/**
	 * Sets the activation listener for the given component.
	 *
	 * @param __stateP The state pointer.
	 * @param __componentP The component pointer.
	 * @param __listener The listener to set.
	 * @throws MLECallError On null arguments; or the state and/or component
	 * are not valid.
	 * @since 2024/07/28
	 */
	static native void __componentSetActivateListener(long __stateP,
		long __componentP, ScritchActivateListener __listener)
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
	static native void __componentSetInputListener(long __stateP,
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
	static native void __componentSetPaintListener(long __stateP,
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
	static native void __componentSetVisibleListener(long __stateP,
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
	static native int __componentWidth(long __stateP,
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
	static native void __containerAdd(long __stateP,
		long __containerP, long __componentP)
		throws MLECallError;
	
	/**
	 * Removes all items from the container.
	 *
	 * @param __stateP The state pointer.
	 * @param __containerP The container pointer.
	 * @throws MLECallError If the state and/or container are invalid.
	 * @since 2024/07/15
	 */
	static native void __containerRemoveAll(long __stateP,
		long __containerP)
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
	static native void __containerSetBounds(long __stateP,
		long __containerP, long __componentP,
		int __x, int __y, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Checks to see if this is a panel only interface. 
	 *
	 * @param __stateP The state pointer.
	 * @return If this is panel only.
	 * @throws MLECallError If the state pointer is invalid.
	 * @since 2024/07/16
	 */
	static native boolean __envIsPanelOnly(long __stateP)
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
	static native long __fontDerive(long __stateP, long __fontP,
		int __style, int __pixelSize)
		throws MLECallError;
	
	/**
	 * Returns the native look and feel color.
	 *
	 * @param __stateP The state pointer.
	 * @param __contextP Optional context pointer.
	 * @param __element The element to request the color of.
	 * @return The resultant color.
	 * @throws MLECallError If the state is not valid; or the color is not
	 * valid.
	 * @since 2024/07/27
	 */
	static native int __lafElementColor(long __stateP, long __contextP,
		int __element)
		throws MLECallError;
	
	/**
	 * Link in the library and load the given structure pointer.
	 *
	 * @param __libPath The library path.
	 * @param __name The interface name.
	 * @return The resultant ScritchUI state pointer.
	 * @since 2024/03/31
	 */
	static native long __linkInit(String __libPath, String __name);
	
	/**
	 * Initializes a new list.
	 *
	 * @param __stateP The state pointer.
	 * @param __type The type of list this is.
	 * @return The resultant list pointer.
	 * @throws MLECallError If the state pointer is invalid or the list could
	 * not be created.
	 * @since 2024/07/16
	 */
	static native long __listNew(long __stateP, int __type)
		throws MLECallError;
	
	/**
	 * Executes the given runnable in the loop.
	 *
	 * @param __stateP The state pointer.
	 * @param __task The task to run.
	 * @since 2024/04/16
	 */
	static native void __loopExecute(long __stateP, Runnable __task);
	
	/**
	 * Executes the given runnable in the loop.
	 *
	 * @param __stateP The state pointer.
	 * @param __task The task to run.
	 * @since 2024/04/16
	 */
	static native void __loopExecuteLater(long __stateP,
		Runnable __task);
	
	/**
	 * Executes the given runnable in the loop.
	 *
	 * @param __stateP The state pointer.
	 * @param __task The task to run.
	 * @since 2024/04/17
	 */
	static native void __loopExecuteWait(long __stateP,
		Runnable __task);
	
	/**
	 * Is this thread in the event loop?
	 *
	 * @param __stateP The state pointer.
	 * @return If this is in the event loop or not.
	 * @since 2024/04/16
	 */
	static native boolean __loopIsInThread(long __stateP);
	
	/**
	 * Creates a hardware reference bracket to the native hardware graphics.
	 * 
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @return The bracket capable of drawing hardware accelerated graphics.
	 * @throws MLECallError If the requested graphics are not valid.
	 * @since 2020/09/25
	 */
	static native PencilBracket __hardwareGraphics(
		long __stateP,
		int __pf,
		int __bw,
		int __bh,
		Object __buf,
		int[] __pal,
		int __sx, int __sy,
		int __sw,
		int __sh)
		throws MLECallError;
	
	/**
	 * Set the label string.
	 *
	 * @param __stateP The state pointer.
	 * @param __labelP The label pointer.
	 * @param __string The string to set.
	 * @throws MLECallError If the string could not be set.
	 * @since 2024/07/21
	 */
	static native void __labelSetString(long __stateP,
		long __labelP, String __string)
		throws MLECallError;
	
	/**
	 * Creates a new menu bar.
	 *
	 * @param __stateP The current state pointer.
	 * @return The resultant menu bar.
	 * @throws MLECallError If it could not be created.
	 * @since 2024/07/20
	 */
	static native long __menuBarNew(long __stateP)
		throws MLECallError;
	
	/**
	 * Adds the given menu item to the menu.
	 *
	 * @param __stateP The state pointer.
	 * @param __intoP The menu to insert into.
	 * @param __at The index to insert at.
	 * @param __itemP The item to be inserted.
	 * @throws MLECallError If the index is not valid or any other pointer
	 * is not valid.
	 * @since 2024/07/23
	 */
	static native void __menuInsert(long __stateP, long __intoP, int __at,
		long __itemP)
		throws MLECallError;
	
	/**
	 * Creates a new menu item.
	 *
	 * @param __stateP The current state pointer.
	 * @return The resultant menu item.
	 * @throws MLECallError If it could not be created.
	 * @since 2024/07/21
	 */
	static native long __menuItemNew(long __stateP)
		throws MLECallError;
	
	/**
	 * Creates a new menu.
	 *
	 * @param __stateP The current state pointer.
	 * @return The resultant menu.
	 * @throws MLECallError If it could not be created.
	 * @since 2024/07/21
	 */
	static native long __menuNew(long __stateP)
		throws MLECallError;
	
	/**
	 * Removes all items from the menu.
	 *
	 * @param __stateP The state pointer.
	 * @param __menuKindP The menu to remove from.
	 * @throws MLECallError On null arguments.
	 * @since 2024/07/23
	 */
	static native void __menuRemoveAll(long __stateP, long __menuKindP)
		throws MLECallError;
	
	/**
	 * Deletes the given object.
	 *
	 * @param __stateP The state pointer.
	 * @param __objectP The object pointer.
	 * @throws MLECallError On null arguments or the object is not valid.
	 * @since 2024/07/20
	 */
	static native void __objectDelete(long __stateP, long __objectP)
		throws MLECallError;
	
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
	static native void __panelEnableFocus(long __stateP,
		long __panelP, boolean __enabled, boolean __default)
		throws MLECallError;
	
	/**
	 * Initializes a new panel. 
	 *
	 * @param __stateP The state pointer.
	 * @return The pointer to the panel.
	 * @since 2024/04/06
	 */
	static native long __panelNew(long __stateP);
	
	/**
	 * Returns the screen ID.
	 *
	 * @param __stateP The state pointer.
	 * @param __screenP The screen pointer.
	 * @return The screen ID.
	 * @throws MLECallError If the screen is not valid.
	 * @since 2024/07/16
	 */
	static native int __screenId(long __stateP, long __screenP)
		throws MLECallError;
	
	/**
	 * Queries the native screens.
	 *
	 * @param __stateP The state pointer.
	 * @param __screenPs The resultant screen pointers.
	 * @return The number of screens returned, may be higher than the input
	 * if there are more screens.
	 * @since 2024/04/15
	 */
	static native int __screens(long __stateP, long[] __screenPs);
	
	/**
	 * Creates a new scroll panel.
	 *
	 * @param __stateP The state pointer.
	 * @return The resultant panel.
	 * @throws MLECallError If the state is invalid; or the panel could not
	 * be created.
	 * @since 2024/07/29
	 */
	static native long __scrollPanelNew(long __stateP)
		throws MLECallError;
	
	/**
	 * Sets the view area of a view.
	 *
	 * @param __stateP The state pointer.
	 * @param __viewP The view pointer.
	 * @param __width The width.
	 * @param __height The height.
	 * @throws MLECallError If the state and/or view are invalid; or the
	 * specified width and/or height are not valid
	 * @since 2024/07/30
	 */
	static native void __viewSetArea(long __stateP, long __viewP, int __width,
		int __height)
		throws MLECallError;
	
	/**
	 * Sets the size suggestion listener for the given view.
	 *
	 * @param __stateP The state pointer.
	 * @param __viewP The view pointer.
	 * @param __listener The listener to set.
	 * @throws MLECallError If the state and/or view are not valid; or the
	 * listener could not be set.
	 * @since 2024/07/29
	 */
	static native void __viewSetSizeSuggestListener(long __stateP,
		long __viewP, ScritchSizeSuggestListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the view listener for the given view.
	 *
	 * @param __stateP The state pointer.
	 * @param __viewP The view pointer.
	 * @param __listener The listener to set.
	 * @throws MLECallError If the state and/or view are not valid; or the
	 * listener could not be set.
	 * @since 2024/07/29
	 */
	static native void __viewSetViewListener(long __stateP, long __viewP,
		ScritchViewListener __listener)
		throws MLECallError;
	
	/**
	 * Deletes the given weak pointer.
	 *
	 * @param __weakP The weak pointer to delete.
	 * @throws MLECallError If it could not be deleted.
	 * @since 2024/07/11
	 */
	static native void __weakDelete(long __weakP)
		throws MLECallError;
	
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
	static native void __windowContentMinimumSize(long __stateP,
		long __windowP, int __w, int __h)
		throws MLECallError;
	
	/**
	 * Returns the {@link ScritchWindowManagerType}.
	 *
	 * @param __stateP The state pointer.
	 * @return A {@link ScritchWindowManagerType}.
	 * @since 2024/04/15
	 */
	static native int __windowManagerType(long __stateP);
	
	/**
	 * Creates a new window.
	 *
	 * @param __stateP The state pointer.
	 * @return The newly created window or {@code 0} if it could not be
	 * created.
	 * @since 2024/04/16
	 */
	static native long __windowNew(long __stateP);
	
	/**
	 * Sets the close listener for a window.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window to set the listener for.
	 * @param __listener The listener to be called on close.
	 * @throws MLECallError If it could not be set.
	 * @since 2024/05/13
	 */
	static native void __windowSetCloseListener(long __stateP,
		long __windowP, ScritchCloseListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the menu bar for the window.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window pointer.
	 * @param __menuBarP The menu bar pointer, may be {@code 0}.
	 * @throws MLECallError If the state or window are not valid; or if the
	 * menu bar could not be added.
	 * @since 2024/07/23
	 */
	static native void __windowSetMenuBar(long __stateP, long __windowP,
		long __menuBarP)
		throws MLECallError;
	
	/**
	 * Sets the activation listener for the given window.
	 *
	 * @param __stateP The state pointer.
	 * @param __windowP The window pointer.
	 * @param __listener The listener to set.
	 * @throws MLECallError If the state and/or window are not valid; or the
	 * listener could not be set.
	 * @since 2024/07/30
	 */
	static native void __windowSetMenuItemActivateListener(long __stateP,
		long __windowP, ScritchMenuItemActivateListener __listener)
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
	static native void __windowSetVisible(long __stateP,
		long __windowP, boolean __visible)
		throws MLECallError;
}
