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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Async;

/**
 * Represents a node within the menu tree.
 *
 * @since 2024/07/18
 */
public final class MenuLayoutBarNode
{
	/** The MIDP item this is for. */
	private final Object _midp;
	
	/** The ScritchUI item. */
	private volatile ScritchBaseBracket _scritch;
	
	/** Submenu items from this node. */
	private final List<MenuLayoutBarNode> _subNodes =
		new ArrayList<>();
	
	/**
	 * Sets the base menu binding association.
	 *
	 * @param __midp The MIDP item to attach to.
	 * @param __scritch The ScritchUI item to attach to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	MenuLayoutBarNode(Object __midp, ScritchBaseBracket __scritch)
		throws NullPointerException
	{
		if (__midp == null || __scritch == null)
			throw new NullPointerException("NARG");
		
		this._midp = __midp;
		this._scritch = __scritch;
	}
	
	/**
	 * Builds the menu.
	 *
	 * @param __scritchApi The ScritchUI API.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	void __build(ScritchInterface __scritchApi)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Clears out the menu.
	 *
	 * @param __scritchApi The ScritchUI API.
	 * @param __delete Should the native menu fragment be deleted?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	void __clear(ScritchInterface __scritchApi, boolean __delete)
		throws NullPointerException
	{
		// Cannot do this again
		if (this._scritch == null)
			throw new IllegalStateException("GONE");
		
		// Clear out nodes recursively
		List<MenuLayoutBarNode> subNodes = this._subNodes;
		synchronized (this)
		{
			// Clear out everything so nothing is left
			for (int n = subNodes.size(), i = n - 1; i >= 0; i--)
			{
				// Clear out node set
				subNodes.get(i).__clear(__scritchApi, true);
				
				// Remove from here, so we just lose it
				subNodes.remove(i);
			}
		}
		
		// Delete native item?
		if (__delete)
		{
			__scritchApi.objectDelete(this._scritch);
			this._scritch = null;
		}
	}
}
