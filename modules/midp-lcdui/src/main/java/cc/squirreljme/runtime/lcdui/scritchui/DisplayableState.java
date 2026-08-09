// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFPlatformFlag;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SpecificFlags;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.microedition.lcdui.Displayable;
import org.intellij.lang.annotations.MagicConstant;

/**
 * State for {@link Displayable}.
 * 
 * To get the state of an arbitrary displayable, assuming it has not been
 * garbage collected, there is {@link DisplayableState#locate(Displayable)}.
 *
 * @since 2024/03/08
 */
@SquirrelJMEVendorApi
public final class DisplayableState
{
	/** The displayable states that exist. */
	@KeepWhenCompacting
	private static final List<Reference<DisplayableState>> _binds =
		new LinkedList<>();
	
	/** The displayable this is linked to. */
	@SquirrelJMEVendorApi
	protected final Reference<Displayable> displayable;
	
	/** The title of this displayable. */
	@SquirrelJMEVendorApi
	protected final StringNotifier title =
		new StringNotifier();
	
	/** The panel to use for this specific displayable. */
	@SquirrelJMEVendorApi
	protected final ScritchPanelBracket panel;
	
	/** The API in use. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface scritchApi;
	
	/** The display this is showing on. */
	@SquirrelJMEVendorApi
	private volatile DisplayState _current;
	
	/** Is full screen being desired? */
	@SquirrelJMEVendorApi
	private volatile boolean _desireFullScreen;
	
	/** Displayable specific flags, defined by {@link Displayable}. */
	@SquirrelJMEVendorApi
	@MagicConstant(flagsFromClass = SpecificFlags.class)
	private volatile int _specificFlags;
	
	/**
	 * Initializes the displayable state.
	 *
	 * @param __displayable The displayable this is linked to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/08
	 */
	@SquirrelJMEVendorApi
	public DisplayableState(Displayable __displayable)
		throws NullPointerException
	{
		if (__displayable == null)
			throw new NullPointerException("NARG");
		
		this.displayable = new WeakReference<>(__displayable);
		
		// Self bind
		synchronized (DisplayableState.class)
		{
			DisplayableState._binds.add(
				new WeakReference<DisplayableState>(this));
		}
		
		// Initialize basic panel
		ScritchInterface scritchApi = DisplayManager.instance().scritch();
		this.scritchApi = scritchApi;
		this.panel = scritchApi.panel().panelNew(); 
	}
	
	/**
	 * Returns the current display.
	 *
	 * @return The current display.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	public final DisplayState currentDisplay()
	{
		synchronized (this)
		{
			return this._current;
		}
	}
	
	/**
	 * Is full-screen is desired?
	 *
	 * @return If fullscreen is desired or not.
	 * @since 2025/12/23
	 */
	@SquirrelJMEVendorApi
	public boolean desireFullScreen()
	{
		synchronized (this)
		{
			return this._desireFullScreen;
		}
	}
	
	/**
	 * Sets if full-screen is desired.
	 *
	 * @param __isFull Should full-screen be used?
	 * @since 2025/12/23
	 */
	@SquirrelJMEVendorApi
	public void desireFullScreen(boolean __isFull)
	{
		synchronized (this)
		{
			this._desireFullScreen = __isFull;
		}
	}
	
	/**
	 * Returns the associated displayable.
	 *
	 * @return The associated displayable.
	 * @throws IllegalStateException If it was garbage collected.
	 * @since 2024/03/08
	 */
	@SquirrelJMEVendorApi
	public final Displayable displayable()
		throws IllegalStateException
	{
		Displayable result = this.displayable.get();
		if (result == null)
			throw new IllegalStateException("GCGC");
		return result;
	}
	
	/**
	 * Returns the {@link SpecificFlags} set on this {@link Displayable}.
	 * 
	 * This should only be called from the event loop, if it is not then
	 * it is very possible for deadlocks to occur.
	 *
	 * @return The {@link SpecificFlags} set on this {@link Displayable}.
	 * @since 2026/08/09
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	@MagicConstant(flagsFromClass = SpecificFlags.class)
	public int flags()
	{
		synchronized (this)
		{
			return this._specificFlags;
		}
	}
	
	/**
	 * Sets the {@link SpecificFlags} flags which are unique to the
	 * {@link Displayable} implementation. 
	 *
	 * This should only be called from the event loop, if it is not then
	 * it is very possible for deadlocks to occur.
	 *
	 * @param __flags The {@link SpecificFlags} to set.
	 * @return The specific flags which were previously set.
	 * @since 2026/08/09
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	@MagicConstant(flagsFromClass = SpecificFlags.class)
	public int flags(
		@MagicConstant(flagsFromClass = SpecificFlags.class) int __flags)
	{
		synchronized (this)
		{
			int old = this._specificFlags;
			this._specificFlags = __flags;
			return old;
		}
	}
	
	/**
	 * Does this use the calculator layout?
	 *
	 * @return If this uses the calculator layout.
	 * @since 2025/05/15
	 */
	@SquirrelJMEVendorApi
	public boolean isCalcLayout()
	{
		return (this.scritchApi.environment()
			.lookAndFeel().lafPlatformFlags() &
			ScritchLAFPlatformFlag.NUMPAD_CALC_LAYOUT) != 0;
	}
	
	/**
	 * Returns the ScritchUI interface in use.
	 *
	 * @return The ScritchUI interface in use.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	public ScritchInterface scritchApi()
	{
		return this.scritchApi;
	}
	
	/**
	 * Returns the ScritchUI panel.
	 *
	 * @return The panel used for this {@link Displayable}.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	public ScritchPanelBracket scritchPanel()
	{
		return this.panel;
	}
	
	/**
	 * Sets the display parent.
	 *
	 * @param __parent The parent display.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	public void setParent(DisplayState __parent)
	{
		Debugging.debugNote("%p.setParent(%p)", this, __parent);
		
		synchronized (this)
		{
			DisplayState current = this.currentDisplay();
			
			// Nothing needs to be done?
			if (__parent == current)
				return;
			
			// Detach old parent?
			if (current != null)
				current.__setCurrent(null);
			
			// Attach to new parent
			this._current = __parent;
			if (__parent != null)
				__parent.__setCurrent(this);
		}
	}
	
	/**
	 * Locates the {@link DisplayableState} associated with the given
	 * {@link Displayable}. This is an expensive call which should be cached
	 * where possible.
	 *
	 * @param __displayable The displayable to locate.
	 * @return The resultant displayable state.
	 * @throws NoSuchElementException If either the {@link DisplayableState}
	 * or {@link Displayable} has been garbage collected, that is nothing
	 * is referencing it strongly.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/08/09
	 */
	@SquirrelJMEVendorApi
	public static DisplayableState locate(Displayable __displayable)
		throws NoSuchElementException, NullPointerException
	{
		if (__displayable == null)
			throw new NullPointerException("NARG");
		
		// This is an actual linked list, so it is slow
		List<Reference<DisplayableState>> binds = DisplayableState._binds;
		synchronized (DisplayableState.class)
		{
			// Go through and cleanup where possible, if we can
			Iterator<Reference<DisplayableState>> it = binds.iterator();
			while (it.hasNext())
			{
				// If the displayable state was GCed, clean it
				DisplayableState state = it.next().get();
				if (state == null)
				{
					it.remove();
					continue;
				}
				
				// Was the displayable itself GCed?
				Displayable result = state.displayable.get();
				if (result == null)
				{
					it.remove();
					continue;
				}
				
				// Is this the correct displayable? If it is then return
				// the state
				if (result == __displayable)
					return state;
			}
		}
		
		// Not found
		throw new NoSuchElementException("GCGC");
	}
}
