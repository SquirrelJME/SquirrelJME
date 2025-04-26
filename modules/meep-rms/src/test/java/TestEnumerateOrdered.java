// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests ordered enumerations.
 *
 * @since 2025/04/23
 */
public class TestEnumerateOrdered
	extends BaseEnumerate
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test(RecordStore __store, int __a, int __b, int __c)
		throws Throwable
	{
		if (true)
			throw new UntestableException("TODO");
		throw Debugging.todo();
	}
}
