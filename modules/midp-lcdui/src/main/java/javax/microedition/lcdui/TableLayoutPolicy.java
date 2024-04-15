// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class TableLayoutPolicy
	extends FormLayoutPolicy
{
	@Api
	public TableLayoutPolicy(Form __f, int __cols)
	{
		super(__f);
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/07
	 */
	@Override
	protected void doLayout(int __viewportX, int __viewportY, int __viewportW,
		int __viewportH, int[] __totalSize)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getColumns()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/07
	 */
	@Override
	protected Item getTraverse(Item __i, int __dir)
	{
		throw Debugging.todo();
	}
}

