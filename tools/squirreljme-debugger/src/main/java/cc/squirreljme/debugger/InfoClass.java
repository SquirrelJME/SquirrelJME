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

/**
 * Caches information on remote classes and otherwise.
 *
 * @since 2024/01/22
 */
public class InfoClass
	extends Info
{
	/**
	 * Initializes the base information.
	 *
	 * @param __id The ID number of this info.
	 * @since 2024/01/22
	 */
	public InfoClass(int __id)
		throws NullPointerException
	{
		super(__id, InfoKind.CLASS);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state,
		Consumer<Info> __callback)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
