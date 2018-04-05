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
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.requests.CollectableCleanup;
import cc.squirreljme.runtime.lcdui.requests.CollectableCreate;
import cc.squirreljme.runtime.lcdui.requests.DisplayVibrate;
import cc.squirreljme.runtime.lcdui.requests.QueryDisplays;

/**
 * This represents a single request to be made by the LCD server, it allows
 * events to be dispatched to the main GUI or event handling thread from
 * other threads so that cross-thread boundaries are kept in check.
 *
 * @since 2018/03/17
 */
public abstract class LcdRequest
	implements Runnable
{
	/** The server performing the call. */
	protected final LcdServer server;
	
	/** The function being executed. */
	protected final LcdFunction function;
	
	/** Exception was thrown. */
	private volatile Throwable _tossed;
	
	/** The return value. */
	private volatile Object _result;
	
	/** Has executed? */
	private volatile boolean _finished;
	
	/**
	 * Initializes the base request.
	 *
	 * @param __sv The owning server.
	 * @param __func The function being called.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public LcdRequest(LcdServer __sv, LcdFunction __func)
		throws NullPointerException
	{
		if (__sv == null || __func == null)
			throw new NullPointerException("NARG");
		
		this.server = __sv;
		this.function = __func;
	}
	
	/**
	 * Invokes the given request.
	 *
	 * @since 2018/03/23
	 */
	protected abstract Object invoke();
	
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
		try
		{
			this._result = this.invoke();
		}
		
		// Failed
		catch (RuntimeException|Error e)
		{
			_tossed = e;
			
			// If this function is not a query then it has internally failed
			// so print the trace
			if (!this.function.query())
				e.printStackTrace();
		}
		
		// Finished execution
		this._finished = true;
	}
	
	/**
	 * Creates a new request which uses the given function and creates an
	 * object that will later execute or run the specified request.
	 *
	 * @param __sv The server which is performing the request.
	 * @param __func The function to execute.
	 * @param __args The arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public static final LcdRequest create(LcdServer __sv,
		LcdFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__sv == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
			case DISPLAY_VIBRATE:
				return new DisplayVibrate(__sv,
					__sv.<UiDisplay>getWidget(UiDisplay.class, (Integer)__args[0]),
					(Integer)__args[1]);
			
			case COLLECTABLE_CREATE:
				return new CollectableCreate(__sv, 
					((EnumType)__args[0]).<CollectableType>asEnum(
					CollectableType.class));
			
			case COLLECTABLE_CLEANUP:
				return new CollectableCleanup(__sv,
					__sv.getCollectable((Integer)__args[0]));
			
			case QUERY_DISPLAYS:
				return new QueryDisplays(__sv, (RemoteMethod)__args[0]);
				
				// {@squirreljme.error EB20 Unimplemented function.
				// (The function)}
			default:
				throw new RuntimeException(String.format("EB20 %s",
					__func));
		}
	}
	
	/**
	 * Returns the UI element for the given type.
	 *
	 * @param <U> The class type to get.
	 * @param __sv The server to get from.
	 * @param __dx The index of the arguments to get.
	 * @param __cl The class type to get.
	 * @param __args The inoput arguments.
	 * @return The UI of the given type.
	 * @since 2018/04/05
	 */
	private static final <U extends UiInterface> __ui(LcdServer __sv,
		int __dx, Class<U> __cl, Object... __args)
	{
		return __sv.<U>get(__cl, __int(__dx, __args));
	}
	
	/**
	 * Returns the integer argument.
	 *
	 * @param __dx The index.
	 * @param __args The input arguments.
	 * @return The integer value.
	 * @since 2018/04/05
	 */
	private static final int __int(int __dx, Object... __args)
	{
		return (Integer)__args[__dx];
	}
}

