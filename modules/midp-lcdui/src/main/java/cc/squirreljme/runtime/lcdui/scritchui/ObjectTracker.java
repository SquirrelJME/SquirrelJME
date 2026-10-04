// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This keeps track of an object string, if it changes then an update to
 * a signal end is emitted.
 * 
 * The listener that is called is always in the event loop.
 *
 * @param <T> The object type.
 * @param <L> The listener type to use.
 * @since 2024/07/18
 */
public abstract class ObjectTracker<T, L>
{
	/** The event loop used. */
	protected final ScritchEventLoopInterface loop;
	
	/** The current value. */
	volatile T _value;
	
	/** The currently attached listener. */
	volatile L _listener;
	
	/**
	 * Initializes the object tracker with the given initial text.
	 *
	 * @param __loop The event loop interface.
	 * @param __init The initial value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public ObjectTracker(ScritchEventLoopInterface __loop, T __init)
		throws NullPointerException
	{
		if (__loop == null)
			throw new NullPointerException("NARG");
		
		this.loop = __loop;
		this._value = __init;
	}
	
	/**
	 * Executes the given listener.
	 *
	 * @param __listener The listener to execute.
	 * @param __value The value being set.
	 * @throws NullPointerException If no listener was specified.
	 * @since 2024/07/20
	 */
	protected abstract void exec(L __listener, T __value)
		throws NullPointerException;
	
	/**
	 * Connects to the given listener.
	 *
	 * @param __listener The listener to connect to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	public final void connect(L __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._listener = __listener;
		}
		
		// Since we connected a listener, we want to make sure it has the
		// most up-to-date information
		this.loop.loopExecute(new __ExecObjectTracker__<T, L>(this));
	}
	
	/**
	 * Gets the current text.
	 *
	 * @return The current text.
	 * @since 2024/07/18
	 */
	public final T get()
	{
		synchronized (this)
		{
			return this._value;
		}
	}
	
	/**
	 * Sets the given text.
	 *
	 * @param __t The text to set.
	 * @since 2024/07/18
	 */
	public final void set(T __t)
	{
		L listener;
		synchronized (this)
		{
			this._value = __t;
			listener = this._listener;
		}
		
		// Inform listener of the change?
		if (listener != null)
			this.loop.loopExecute(new __ExecObjectTracker__<T, L>(
				this));
	}
}
