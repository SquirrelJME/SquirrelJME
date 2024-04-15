// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * This wraps a native {@link UIItemBracket}.
 *
 * @since 2020/07/18
 */
public final class UIItemObject
	extends UIWidgetObject
{
	/** The item to wrap. */
	public final UIItemBracket item;
	
	/**
	 * Initializes the item object.
	 *
	 * @param __machine The owning machine.
	 * @param __item The item to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/18
	 */
	public UIItemObject(SpringMachine __machine, UIItemBracket __item)
		throws NullPointerException
	{
		super(__machine, UIItemBracket.class);
		
		if (__item == null)
			throw new NullPointerException("NARG");
		
		this.item = __item;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/20
	 */
	@Override
	public UIItemBracket widget()
	{
		return this.item;
	}
}
