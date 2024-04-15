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
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

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
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	__CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __SpacerState__(__backend, this);
	}
	
	/**
	 * Spacer state.
	 * 
	 * @since 2023/01/14
	 */
	static class __SpacerState__
		extends Item.__ItemState__
	{
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__SpacerState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
		}
	}
}


