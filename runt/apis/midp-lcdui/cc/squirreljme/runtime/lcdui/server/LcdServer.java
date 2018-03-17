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
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.LcdFunction;

/**
 * This class implements the base for the LCDUI interface used for the
 * server end which resides in the kernel.
 *
 * @since 2018/03/16
 */
public abstract class LcdServer
	implements ServiceServer
{
	/** The task this provides a service for. */
	protected final SystemTask task;
	
	/**
	 * Initializes the base server for the task.
	 *
	 * @param __task The task this provides a service for.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/16
	 */
	public LcdServer(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
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
		
		// Depends on the function
		LcdFunction func;
		switch ((func = __func.<LcdFunction>asEnum(LcdFunction.class)))
		{
				// {@squirreljme.error EB1u Unknown or unimplemented LCDUI
				// function. (The LCD function)}
			default:
				throw new RuntimeException(String.format("EB1u %s", func));
		}
	}
}

