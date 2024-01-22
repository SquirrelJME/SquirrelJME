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
 * Views remote instructions.
 *
 * @since 2024/01/22
 */
public class RemoteInstructionViewer
	implements InstructionViewer
{
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public int address()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public String mnemonic()
	{
		throw Debugging.todo();
	}
}
