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
import net.multiphasicapps.classfile.ClassName;

/**
 * Views remote classes.
 *
 * @since 2024/01/22
 */
public class RemoteClassViewer
	implements ClassViewer
{
	/**
	 * Initializes the remote class viewer.
	 *
	 * @param __state The state for communication and otherwise.
	 * @param __classInfo The class to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public RemoteClassViewer(DebuggerState __state, InfoClass __classInfo)
		throws NullPointerException
	{
		if (__classInfo == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public MethodViewer[] methods()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public ClassName thisName()
	{
		throw Debugging.todo();
	}
}
