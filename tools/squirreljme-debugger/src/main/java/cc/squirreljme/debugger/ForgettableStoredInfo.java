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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stored information that is forgettable and is cached by weak references.
 *
 * @param <I> The type of information to store.
 * @since 2024/01/25
 */
public class ForgettableStoredInfo<I extends Info>
	extends StoredInfo<I>
{
	/** The internal item cache. */
	private final Map<JDWPId, Reference<I>> _cache =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the stored info.
	 *
	 * @param __type The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public ForgettableStoredInfo(InfoKind __type)
		throws NullPointerException
	{
		super(__type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public I[] all(DebuggerState __state, Class<? extends Info[]> __basis)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/25
	 */
	@Override
	public I get(DebuggerState __state, JDWPId __id, Object... __extra)
	{
		throw Debugging.todo();
	}
}
