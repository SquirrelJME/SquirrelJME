// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Encoder for Shift-JIS.
 *
 * @since 2022/07/12
 */
public class ShiftJisEncoder
	extends ShiftJisBase
	implements Encoder
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public int encode(char __c, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
	}
}
