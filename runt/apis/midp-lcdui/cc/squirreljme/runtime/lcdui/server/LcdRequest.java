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

import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
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
	/** The server performing the action. */
	protected final LcdServer server;
	
	/** The function to execute. */
	protected final LcdFunction function;
	
	/** The arguments to the function. */
	private final Object[] _args;
	
	/** Exception was thrown. */
	private volatile Throwable _tossed;
	
	/** The return value. */
	private volatile Object _result;
	
	/** Has executed? */
	private volatile boolean _finished;
	
	/**
	 * Initializes a request to the LCD display server.
	 *
	 * @param __server The server which is performing the request.
	 * @param __func The function to execute.
	 * @param __args The arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdRequest(LcdServer __server, LcdFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__server == null || __func == null)
			throw new NullPointerException("NARG");
		
		this.server = __server;
		this.function = __func;
		this._args = (__args == null ? new Object[0] : __args.clone());
	}
	
	/**
	 * Returns the result of the request.
	 *
	 * @param <R> The type to return.
	 * @param __cl The type to return.
	 * @return The request result.
	 * @throws Error If the request threw an {@link Error}.
	 * @throws RuntimeException If the request threw a
	 * {@link RuntimeException}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public final <R> R result(Class<R> __cl)
		throws Error, RuntimeException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB1z Execution has not yet finished.}
		if (!this._finished)
			throw new IllegalStateException("EB1z");
		
		// Threw an exception?
		Throwable t = this._tossed;
		if (t instanceof RuntimeException)
			throw (RuntimeException)t;
		else if (t instanceof Error)
			throw (Error)t;
		
		// Return the specified value
		return __cl.cast(this._result);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public final void run()
	{
		// Could fail
		LcdFunction func = this.function;
		try
		{
			Object[] args = this._args;
			
			// Run function and store the result
			Object result = VoidType.INSTANCE;
			switch (func)
			{
				case CREATE_DISPLAYABLE:
					result = this.__createDisplayable(
						((EnumType)args[0]).<DisplayableType>asEnum(
						DisplayableType.class));
					break;
					
				case DISPLAYABLE_SET_TITLE:
					this.__displayableSetTitle((Integer)args[0],
						(String)args[1]);
					break;
				
				case QUERY_DISPLAYS:
					result = this.__queryDisplays();
					break;
				
					// {@squirreljme.error EB20 Unimplemented function.
					// (The function)}
				default:
					throw new RuntimeException(String.format("EB20 %s",
						func));
			}
			
			// Set
			this._result = result;
		}
		
		// Failed
		catch (RuntimeException|Error e)
		{
			_tossed = e;
			
			// If this function is not a query then it has internally failed
			// so print the trace
			if (!func.query())
				e.printStackTrace();
		}
		
		// Finished execution
		this._finished = true;
	}
	
	/**
	 * Creates a new displayable of the given type.
	 *
	 * @param __t The type of displayable to create.
	 * @return The handle to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	private final int __createDisplayable(DisplayableType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		LcdServer server = this.server;
		LcdDisplayable disp = server.state().displayables().
			createDisplayable(server.task(), __t);
		return disp.handle();
	}
	
	/**
	 * Sets the title of the given displayable.
	 *
	 * @param __handle The handle of the displayable.
	 * @param __title The title to use, {@code null} clears it.
	 * @since 2018/03/17
	 */
	private final void __displayableSetTitle(int __handle, String __title)
	{
		LcdServer server = this.server;
		server.state().displayables().getDisplayable(server, __handle).
			setTitle(__title);
	}
	
	/**
	 * Queries the displays which are currently available.
	 *
	 * @return The available displays.
	 * @since 2018/03/17
	 */
	private final IntegerArray __queryDisplays()
	{
		// Querie all displays
		LcdDisplay[] ld = this.server.state().displays().queryDisplays();
		
		// Map indexes
		int n = ld.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = ld[i].index();
		
		return new LocalIntegerArray(rv);
	}
}

