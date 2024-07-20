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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;
import org.jetbrains.annotations.Async;

/**
 * Represents the layout state for a menu bar.
 * 
 * It might be awkward that menu bars are bound to {@link Displayable}s,
 * however {@link Displayable}s always have just a single menu bar.
 * 
 * There is a single pinned menu which is used as the default menu for
 * commands that are added, since they need to go somewhere.
 *
 * @see ScritchMenuBarBracket
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public final class MenuLayoutBar
	extends MenuLayoutBindableSub<Displayable>
{
	/** Root node which represents the menu bar. */
	private volatile MenuLayoutBarNode _rootNode;
	
	/** The pinned menu. */
	private volatile Menu _pinned;
	
	/**
	 * Initializes the bindable.
	 *
	 * @param __scritch The ScritchUI interface.
	 * @param __item The item to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	public MenuLayoutBar(ScritchInterface __scritch, Displayable __item)
		throws NullPointerException
	{
		super(__scritch, __item);
	}
	
	/**
	 * Returns the pinned menu, which generally is the default item.
	 *
	 * @return The pinned menu, or {@code null} if there is nothing pinned.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public Menu pin()
	{
		synchronized (this)
		{
			return this._pinned;
		}
	}
	
	/**
	 * Pins the given menu as the default menu, or clears it.
	 *
	 * @param __menu The menu to pin as the default, {@code null} will clear
	 * the menu.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public void pin(Menu __menu)
	{
		synchronized (this)
		{
			this._pinned = __menu;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	@SquirrelJMEVendorApi
	@Async.Execute
	protected void refreshInLoop()
		throws IllegalStateException
	{
		ScritchInterface scritchApi = this.scritchApi;
		synchronized (this)
		{
			// Do we need to make the root node?
			MenuLayoutBarNode rootNode = this._rootNode;
			if (rootNode == null)
			{
				// Create native menu bar first
				ScritchMenuBarBracket bar = scritchApi.menu().menuBarNew();
				
				// Assign
				rootNode = new MenuLayoutBarNode(this.getMidp(), bar);
				this._rootNode = rootNode;
			}
			
			// Clear out entire tree, so we rebuild the menu
			rootNode.__clear(scritchApi, false);
			
			// Then rebuild in loop
			rootNode.__build(scritchApi);
		}
	}
}
