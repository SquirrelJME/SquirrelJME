// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Virtual machine frame.
 *
 * @since 2023/12/05
 */
public final class NvmFrame
	implements Pointer
{
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/05
	 */
	@Override
	public long pointerAddress()
	{
		throw Debugging.todo();
	}
}
