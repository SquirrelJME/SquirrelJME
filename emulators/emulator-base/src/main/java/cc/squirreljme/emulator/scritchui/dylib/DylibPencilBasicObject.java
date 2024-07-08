// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for basic pencil buffers.
 *
 * @since 2024/07/08
 */
public class DylibPencilBasicObject
	implements DylibPencilObject
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/08
	 */
	@Override
	public long objectPointer()
	{
		throw Debugging.todo();
	}
}
