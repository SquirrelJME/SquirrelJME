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

/**
 * Not Described.
 *
 * @since 2024/01/22
 */
public class InfoMethod
	extends Info
{
	/**
	 * Initializes the method information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @since 2024/01/22
	 */
	public InfoMethod(DebuggerState __state, RemoteId __id)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.METHOD);
	}
	
	/**
	 * {@inheritDoc}*
	 * @since 2024/01/22
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		return true;
	}
}
