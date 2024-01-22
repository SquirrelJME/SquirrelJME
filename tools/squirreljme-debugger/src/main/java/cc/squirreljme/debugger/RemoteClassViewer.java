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
