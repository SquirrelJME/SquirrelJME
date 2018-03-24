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
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdFunctionInterrupted;
import cc.squirreljme.runtime.lcdui.WidgetType;
import java.util.HashMap;
import java.util.Map;

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
	
	/** Widgets which are currently available to this server. */
	private final Map<Integer, LcdWidget> _widgets =
		new HashMap<>();
	
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
	 * Obtains a widget by an ID using a generic class type.
	 *
	 * @param __dx The handle of the widget.
	 * @return The widget of the specified handle or {@code null} if it does
	 * not exist.
	 * @since 2018/03/23
	 */
	public final LcdWidget getWidget(int __dx)
	{
		return this.<LcdWidget>getWidget(LcdWidget.class, __dx);
	}
	
	/**
	 * Obtains the widget by the specified index and class type.
	 *
	 * @param <W> The class type to lookup.
	 * @param __cl The class type to lookup.
	 * @param __dx The handle of the widget.
	 * @return The widget of the specified handle and class type or
	 * {@code null} if the widget does not exist or is not of the specified
	 * class type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public final <W extends LcdWidget> W getWidget(Class<LcdWidget> __cl,
		int __dx)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
		LcdRequest r = LcdRequest.create(this, func, __args);
		
		// If the function is a query then execute it now and return a value
		LcdDisplays rh = this.displays;
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

