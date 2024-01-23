// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * The type of information stored.
 *
 * @since 2024/01/20
 */
public enum InfoKind
{
	/** A thread. */
	THREAD("Thread")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/20
		 */
		@Override
		protected Info seed(DebuggerState __state, RemoteId __id,
			Object... __extra)
		{
			return new InfoThread(__state, __id);
		}
	},
	
	/* Class type. */
	CLASS("Class")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/22
		 */
		@Override
		protected Info seed(DebuggerState __state, RemoteId __id,
			Object... __extra)
		{
			return new InfoClass(__state, __id);
		}
	},
	
	/** Method. */
	METHOD("Method")
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/22
		 */
		@Override
		protected Info seed(DebuggerState __state, RemoteId __id,
			Object... __extra)
		{
			return new InfoMethod(__state, __id);
		}
	},
	
	/* End. */
	;
	
	/** The item description. */
	protected final String description;
	
	/**
	 * The description of this item. 
	 *
	 * @param __desc The item description.
	 * @since 2024/01/20
	 */
	InfoKind(String __desc)
	{
		this.description = __desc;
	}
	
	/**
	 * Seeds an item with the given ID.
	 *
	 * @param __state The state of the debugger.
	 * @param __id The ID of the item.
	 * @param __extra Extra values used by the seed, as some require the
	 * type to have such information.
	 * @return The resultant item.
	 * @since 2024/01/20
	 */
	protected abstract Info seed(DebuggerState __state, RemoteId __id,
		Object... __extra);
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public String toString()
	{
		return this.description;
	}
}
