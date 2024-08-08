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
	/** The pencil pointer. */
	private final long _pencilP;
	
	/** The weak pointer. */
	private final long _weakP;
	
	/**
	 * Initializes the basic pencil.
	 *
	 * @param __pencilP The pencil pointer.
	 * @param __weakP The weak pointer to the pencil.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/11
	 */
	DylibPencilBasicObject(long __pencilP, long __weakP)
		throws NullPointerException
	{
		if (__pencilP == 0 || __weakP == 0)
			throw new NullPointerException("NARG");
		
		this._pencilP = __pencilP;
		this._weakP = __weakP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/08
	 */
	@Override
	public long objectPointer()
	{
		return this._pencilP;
	}
}
