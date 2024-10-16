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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Displayable;

/**
 * State for {@link Displayable}.
 *
 * @since 2024/03/08
 */
@SquirrelJMEVendorApi
public final class DisplayableState
{
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
}
