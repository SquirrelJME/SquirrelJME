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

import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdFunctionInterrupted;

/**
 * This class implements the base for the LCDUI interface used for the
 * server end which resides in the kernel.
 *
 * @since 2018/03/16
 */
public final class LcdServer
	implements ServiceServer
{
	/** The task this provides a service for. */
	protected final SystemTask task;
	
	/** The displays which are available. */
	protected final LcdDisplays displays;
	
	/**
	 * Initializes the LCDUI server.
	 *
	 * @param __s The state of the server.
	 * @param __ld The displays which are available for usage.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdServer(SystemTask __task, LcdDisplays __ld)
		throws NullPointerException
	{
		if (__task == null || __ld == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.displays = __ld;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	public final Object serviceCall(EnumType __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		if (__args == null)
			__args = new Object[0];
		
		// Build request to run in the future
		LcdFunction func = __func.<LcdFunction>asEnum(LcdFunction.class);
		LcdRequest r = new LcdRequest(this, func, __args);
		
		// If the function is a query then execute it now and return a value
		LcdRequestHandler rh = this.state.requestHandler();
		if (func.query())
			for (;;)
				try
				{
					return rh.<Object>invokeNow(Object.class, r);
				}
				catch (InterruptedException e)
				{
					// {@squirreljme.error EB1y The operation was interrupted.}
					if (func.isInterruptable())
						throw new LcdFunctionInterrupted("EB1y", e);
				}
		
		// Otherwise execute it at some later time
		else
		{
			rh.invokeLater(r);
			return VoidType.INSTANCE;
		}
	}
	
	/**
	 * Returns the state of this server.
	 *
	 * @return The server state.
	 * @since 2018/03/18
	 */
	public final LcdState state()
	{
		return this.state;
	}
	
	/**
	 * Returns the task which owns this server.
	 *
	 * @return The owning task.
	 * @since 2018/03/18
	 */
	public final SystemTask task()
	{
		return this.task;
	}
}

