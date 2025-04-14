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
public class Spacer
	extends Item
{
	@Api
	public Spacer(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Commands are not supported for this item.
	 *
	 * @param __a Ignored.
	 * @throws IllegalStateException Always.
	 * @since 2018/04/06
	 */
	@Override
	public void addCommand(Command __a)
		throws IllegalStateException
	{
		/* {@squirreljme.error EB2l Cannot add commands for spacers.} */
		throw new IllegalStateException("EB2l");
	}
	
	/**
	 * Commands are not supported for this item.
	 *
	 * @param __a Ignored.
	 * @throws IllegalStateException Always.
	 * @since 2018/04/06
	 */
	@Override
	public void setDefaultCommand(Command __a)
		throws IllegalStateException
	{
		/* {@squirreljme.error EB2m Cannot set the default command for
		spacers.} */
		throw new IllegalStateException("EB2m");
	}
	
	/**
	 * Labels are not supported for this item.
	 *
	 * @param __a Ignored.
	 * @throws IllegalStateException Always.
	 * @since 2018/04/06
	 */
	@Override
	public void setLabel(String __a)
		throws IllegalStateException
	{
		/* {@squirreljme.error EB2n Cannot set labels for spacers.} */
		throw new IllegalStateException("EB2n");
	}
	
	@Api
	public void setMinimumSize(int __a, int __b)
	{
		throw Debugging.todo();
	}
}


