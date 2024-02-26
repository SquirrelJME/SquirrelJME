// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * Information storage for types and otherwise that are known to the
 * debugger.
 *
 * @since 2024/01/19
 */
public abstract class Info
	implements Comparable<Info>
{
	/** The ID of this item. */
	protected final JDWPId id;
	
	/** The kind of info this is. */
	protected final InfoKind kind;
	
	/** The state cache. */
	private final Reference<DebuggerState> _state;
	
	/** Has this item been disposed of? */
	private volatile boolean _disposed;
	
	/**
	 * Initializes the base information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __kind The kind of information this is.
	 * @since 2024/01/20
	 */
	public Info(DebuggerState __state, JDWPId __id, InfoKind __kind)
		throws NullPointerException
	{
		if (__state == null || __kind == null)
			throw new NullPointerException("NARG");
		
		this._state = new WeakReference<>(__state);
		this.id = __id;
		this.kind = __kind;
	}
	
	/**
	 * Internal update logic for this item.
	 *
	 * @param __state The state to update in.
	 * @return Will return {@code true} if the info is not disposed.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	protected abstract boolean internalUpdate(DebuggerState __state)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public int compareTo(@NotNull Info __o)
	{
		if (this == __o)
			return 0;
		
		// If the classes are different, just sort the name
		Class<?> a = this.getClass();
		Class<?> b = __o.getClass();
		if (a != b)
			return a.getName().compareTo(b.getName());
		
		// Compare by ID
		return this.id.compareTo(__o.id);
	}
	
	/**
	 * Disposes this item to try to get rid of it.
	 *
	 * @since 2024/01/21
	 */
	protected final void dispose()
	{
		synchronized (this)
		{
			this._disposed = true;
			
			// Notify the monitor
			this.notifyAll();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		else if (!(__o instanceof Info))
			return false;
		
		return this.compareTo((Info)__o) == 0;
	}
	
	/**
	 * Returns the attached debugger state.
	 *
	 * @return The debugger state.
	 * @since 2024/01/22
	 */
	protected DebuggerState internalState()
	{
		DebuggerState result = this._state.get();
		if (result == null)
			throw new IllegalStateException("GCGC");
		
		return result;
	}
	
	/**
	 * Returns the internal string representation.
	 *
	 * @return The string to use.
	 * @since 2024/01/20
	 */
	protected String internalString()
	{
		return null;
	}
	
	/**
	 * Has this item been disposed of?
	 *
	 * @return The resultant item.
	 * @since 2024/01/21
	 */
	public final boolean isDisposed()
	{
		synchronized (this)
		{
			return this._disposed;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public String toString()
	{
		// Check if disposed first
		synchronized (this)
		{
			if (this._disposed)
				return String.format("DISPOSED %s#%s", this.kind, this.id);
		}
		
		// Has this been disposed?
		try
		{
			String internalString = this.internalString();
			synchronized (this)
			{
				if (this._disposed)
					return String.format("DISPOSED %s#%s", this.kind, this.id);
				
				else if (internalString != null)
					return internalString;
			}
		}
		catch (Throwable __t)
		{
			__t.printStackTrace();
		}
		
		return String.format("%s#%s", this.kind, this.id);
	}
	
	/**
	 * Requests that the debugger update information about this.
	 *
	 * @param __state The state to update in.
	 * @param __callback The callback to use when an update is complete.
	 * @return Will return {@code true} if the info is not disposed.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public final boolean update(DebuggerState __state,
		Consumer<Info> __callback)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		// If this has been disposed of, do nothing
		synchronized (this)
		{
			if (this._disposed)
				return false;
		}
		
		// Do not do any update if the VM has yet to start, assume it has not
		// been disposed at all in this state
		/*if (!__state.hasStarted())
			return true;*/
		
		// Internal update, if it becomes disposed then set as such
		boolean result = this.internalUpdate(__state);
		if (!result)
			this.dispose();
		
		// Send it to the callback
		else if (__callback != null)
			__callback.accept(this);
		
		// Run the check again since the information could have been updated
		return !this.isDisposed();
	}
}
