// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Executes adjustments to the default menu which contains commands.
 *
 * @since 2024/07/18
 */
class __ExecDisplayableDefaultCommand__
	implements Runnable
{
	/** Is this command being added? */
	private final boolean _add;
	
	/** The command to add or remove. */
	private final Command _command;
	
	/** The displayable to modify. */
	private final Displayable _displayable;
	
	/**
	 * Initializes the execution handler.
	 *
	 * @param __displayable The displayable to adjust the menu of.
	 * @param __command The command being manipulated.
	 * @param __add Is this command being added or removed?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	__ExecDisplayableDefaultCommand__(Displayable __displayable,
		Command __command, boolean __add)
		throws NullPointerException
	{
		if (__displayable == null || __command == null)
			throw new NullPointerException("NARG");
		
		this._displayable = __displayable;
		this._command = __command;
		this._add = __add;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	public void run()
	{
		throw Debugging.todo();
	}
}
