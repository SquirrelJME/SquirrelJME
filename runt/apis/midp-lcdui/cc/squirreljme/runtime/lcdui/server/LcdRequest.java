// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.LcdFunction;

/**
 * This represents a single request to be made by the LCD server, it allows
 * events to be dispatched to the main GUI or event handling thread from
 * other threads so that cross-thread boundaries are kept in check.
 *
 * @since 2018/03/17
 */
public final class LcdRequest
	implements Runnable
{
	/** The task performing the action. */
	protected final SystemTask task;
	
	/** The function to execute. */
	protected final LcdFunction function;
	
	/** The arguments to the function. */
	private final Object[] _args;
	
	/**
	 * Initializes a request to the LCD display server.
	 *
	 * @param __task The task which is performing the request.
	 * @param __func The function to execute.
	 * @param __args The arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdRequest(SystemTask __task, LcdFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__task == null || __func == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.function = __func;
		this._args = (__args == null ? new Object[0] : __args.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public final void run()
	{
		throw new todo.TODO();
	}
}

