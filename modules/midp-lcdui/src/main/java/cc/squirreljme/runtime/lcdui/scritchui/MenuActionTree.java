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
import cc.squirreljme.jvm.mle.scritchui.ScritchLabelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchMenuInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuKindBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasChildrenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasLabelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasParentBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;
import org.jetbrains.annotations.Async;

/**
 * Menu action tree.
 *
 * @since 2024/07/21
 */
@SquirrelJMEVendorApi
public final class MenuActionTree
{
	/** Mapping of nodes to leafs, to keep track of natives. */
	@SquirrelJMEVendorApi
	private final List<Leaf> _mappings =
		new ArrayList<>();
	
	/**
	 * Finds the given item.
	 *
	 * @param __kind The menu kind.
	 * @return The leaf for the given item, or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/30
	 */
	public Leaf find(ScritchMenuKindBracket __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		List<Leaf> mappings = this._mappings;
		synchronized (this)
		{
			for (int i = 0, n = mappings.size(); i < n; i++)
			{
				Leaf leaf = mappings.get(i);
				if (leaf._scritch == __kind)
					return leaf;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Returns the mapping for a given node.
	 *
	 * @param __node The node to get for.
	 * @return The resultant mapping.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	public final Leaf map(MenuActionNode __node)
		throws NullPointerException
	{
		if (__node == null)
			throw new NullPointerException("NARG");
		
		List<Leaf> mappings = this._mappings;
		synchronized (this)
		{
			// Find the mapping node
			int n = mappings.size();
			for (int i = 0; i < n; i++)
			{
				Leaf check = mappings.get(i);
				if (check._node == __node)
					return check;
			}
			
			// Otherwise it needs creation
			Leaf result = new Leaf(__node);
			
			// Add to mapping set
			mappings.add(result);
			
			// And then use it
			return result;
		}
	}
	
	/**
	 * Performs actual update of the menu tree.
	 *
	 * @param __context The base context root.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	public void update(MenuActionNode __context)
		throws NullPointerException
	{
		if (__context == null)
			throw new NullPointerException("NARG");
		
		// Internal recursive logic setup
		this.__update(__context, __context.children());
	}
	
	/**
	 * Adds the given children to this given node.
	 *
	 * @param __into The node to write into.
	 * @param __add The children to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/21
	 */
	@Async.Execute
	private void __update(MenuActionNode __into, MenuActionHasParent... __add)
		throws NullPointerException
	{
		if (__into == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("Menu __update(%s, %s)",
			__into.owner(), (__add == null ? null : Arrays.asList(__add)));
		
		// API for accessing menus
		ScritchInterface scritchApi = DisplayManager.instance().scritch();
		ScritchLabelInterface labelApi = scritchApi.label();
		ScritchMenuInterface menuApi = scritchApi.menu();
		
		// Map ourself into a node
		Leaf into = this.map(__into);
		ScritchMenuKindBracket scritch = into._scritch;
		
		// Children need to be added?
		if (__add != null)
		{
			// Debug
			Debugging.debugNote("Menu add children %d...",
				__add.length);
			
			// Clear everything from the menu beforehand
			menuApi.removeAll((ScritchMenuHasChildrenBracket)scritch);
			
			// Map all menu nodes to leaves first, so that they have created
			// ScritchUI objects as required... if applicable
			int n = __add.length;
			Leaf[] leaves = new Leaf[n];
			for (int i = 0; i < n; i++)
				leaves[i] = this.map(((MenuActionNodeOnly)__add[i])._menuNode);
			
			// Setup leaves first
			for (int i = 0; i < n; i++)
			{
				Leaf leaf = leaves[i];
				MenuActionNode leafNode = leaf._node;
				
				// Recursive update of child
				this.__update(leafNode, leafNode.childrenOptional());
				
				// Add leaf node widget to our own widget
				menuApi.menuInsert(
					(ScritchMenuHasChildrenBracket)scritch, i,
					(ScritchMenuHasParentBracket)leaf._scritch);
			}
		}
		
		// Set text label for this item?
		if (scritch instanceof ScritchMenuHasLabelBracket)
		{
			MenuAction action = into._node.owner(MenuAction.class);
			
			// Set the preferred label to use
			String label = action._longLabel.get();
			if (label == null || label.isEmpty())
				label = action._shortLabel.get();
			
			labelApi.setString((ScritchMenuHasLabelBracket)scritch, label);
		}
	}
	
	/**
	 * A single leaf within the tree.
	 * 
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	public static final class Leaf
	{
		/** The node this maps to. */
		@SquirrelJMEVendorApi
		final MenuActionNode _node;
		
		/** The ScritchUI bracket used. */
		@SquirrelJMEVendorApi
		final ScritchMenuKindBracket _scritch;
		
		/**
		 * Initializes the leaf from the node.
		 *
		 * @param __node The node to map to.
		 * @throws NullPointerException On null arguments.
		 * @since 2024/07/21
		 */
		@SquirrelJMEVendorApi
		Leaf(MenuActionNode __node)
			throws NullPointerException
		{
			if (__node == null)
				throw new NullPointerException("NARG");
			
			MenuActionApplicable owner = __node.owner();
			ScritchInterface api = DisplayManager.instance().scritch();
			
			// Determine the item that should be created
			ScritchMenuKindBracket scritch;
			if (owner instanceof Displayable)
				scritch = api.menu().menuBarNew();
			else if (owner instanceof Menu)
				scritch = api.menu().menuNew();
			else
				scritch = api.menu().menuItemNew();
			
			// Store
			this._node = __node;
			this._scritch = scritch;
		}
		
		/**
		 * Returns the leaf's owner.
		 *
		 * @return The leaf's owner.
		 * @since 2024/07/30
		 */
		public final MenuActionApplicable owner()
		{
			return this._node.owner();
		}
		
		/**
		 * Returns the native widget.
		 *
		 * @param <K> The widget type.
		 * @param __cl The widget type.
		 * @return The resultant widget.
		 * @throws NullPointerException On null arguments.
		 * @since 2024/07/23
		 */
		public <K extends ScritchMenuKindBracket> K scritchWidget(
			Class<? extends K> __cl)
			throws NullPointerException
		{
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			return __cl.cast(this._scritch);
		}
	}
}
