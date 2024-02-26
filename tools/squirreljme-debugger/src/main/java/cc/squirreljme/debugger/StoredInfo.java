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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Information storage for a single type.
 *
 * @param <I> The info type.
 * @since 2024/01/20
 */
public abstract class StoredInfo<I extends Info>
{
	/** The type being stored. */
	protected final InfoKind type;
	
	/**
	 * Initializes the stored info.
	 *
	 * @param __type The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public StoredInfo(InfoKind __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * Returns all the known values.
	 *
	 * @param __state The debugger state.
	 * @param __basis The basis type.
	 * @return All the known values.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public abstract I[] all(DebuggerState __state,
		Class<? extends Info[]> __basis);
	
	/**
	 * Obtains the given item if it is already known if it is not then it
	 * is created accordingly.
	 *
	 * @param __state Optional state, if passed then there will be an implicit
	 * update to the added item.
	 * @param __id The ID of the item to get.
	 * @param __extra Any extra values if this needs to be seeded.
	 * @return The item, if this returns {@code null} then the item was likely
	 * disposed of.
	 * @since 2024/01/20
	 */
	public abstract I get(DebuggerState __state, JDWPId __id,
		Object... __extra);
	
	/**
	 * Returns all the known values.
	 *
	 * @param __state The debugger state.
	 * @return All the known values.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public final Info[] all(DebuggerState __state)
	{
		return this.all(__state, Info[].class);
	}
	
	/**
	 * Obtains the given item if it is already known if it is not then it
	 * is created accordingly.
	 *
	 * @param __id The ID of the item to get.
	 * @return The item, this will never be {@code null} as one is always
	 * created.
	 * @since 2024/01/20
	 */
	public final I get(JDWPId __id)
	{
		return this.get(null, __id);
	}
}
