// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents a command which is used as an abstract representation of
 * a command which may be executed by the client.
 *
 * @since 2018/04/04
 */
public final class UiCommand
	implements UiHasLabel, UiCollectable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final void cleanup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final CollectableType collectableType()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/04
	 */
	@Override
	public final int handle()
	{
		throw new todo.TODO();
	}
}

