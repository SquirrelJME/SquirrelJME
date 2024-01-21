// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	protected final int id;
	
	/** The kind of info this is. */
	protected final InfoKind kind;
	
	/**
	 * Initializes the base information.
	 *
	 * @param __id The ID number of this info.
	 * @param __kind The kind of information this is.
	 * @since 2024/01/20
	 */
	public Info(int __id, InfoKind __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.kind = __kind;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public int compareTo(@NotNull Info __o)
	{
		// If the classes are different, just sort the name
		Class<?> a = this.getClass();
		Class<?> b = __o.getClass();
		if (a != b)
			return a.getName().compareTo(b.getName());
		
		// Compare by ID
		return this.id - __o.id;
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
	 * Requests that the debugger update information about this.
	 *
	 * @param __state The state to update in.
	 * @param __callback The callback to use when an update is complete.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public abstract void update(DebuggerState __state,
		Consumer<Info> __callback)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public String toString()
	{
		String internalString = this.internalString();
		
		if (internalString != null)
			return String.format("%s (%s#%d)",
				internalString, this.kind, this.id);
		return String.format("%s#%d",
			this.kind, this.id);
	}
}
