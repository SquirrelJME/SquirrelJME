// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * A bindable menu that can also contain submenu items.
 *
 * @param <M> The MIDP menu type.
 * @param <S> The ScritchUI bracket type.
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public abstract class MenuLayoutBindableSub<M, S extends ScritchBaseBracket>
	extends MenuLayoutBindable<M, S>
{
	/**
	 * Inserts the given menu action.
	 *
	 * @param __at The index to add at, {@link Integer#MAX_VALUE} means to
	 * add at the very end of the menu.
	 * @param __action The menu action to add.
	 * @return The index the item was added at, in most cases this will match
	 * the value of {@code __at} except in the case it specifies the end of
	 * the menu.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public int insert(int __at, MenuAction<?> __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		if (__at < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			throw Debugging.todo();
		}
	}
}
