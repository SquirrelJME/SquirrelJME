// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;

public class Spacer
	extends Item
{
	public Spacer(int __a, int __b)
	{
		super();
		throw new todo.TODO();
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
		// {@squirreljme.error EB2m Cannot add commands for spacers.}
		throw new IllegalStateException("EB2m");
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
		// {@squirreljme.error EB2l Cannot set the default command for
		// spacers.}
		throw new IllegalStateException("EB2l");
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
		// {@squirreljme.error EB2k Cannot set labels for spacers.}
		throw new IllegalStateException("EB2k");
	}
	
	public void setMinimumSize(int __a, int __b)
	{
		throw new todo.TODO();
	}
}


