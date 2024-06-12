// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;

/**
 * Represents a {@link PencilFontBracket}.
 *
 * @since 2024/06/12
 */
public class DylibPencilFontObject
	extends DylibBaseObject
{
	/**
	 * Initializes the base object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/12
	 */
	public DylibPencilFontObject(long __objectP)
		throws NullPointerException
	{
		super(__objectP);
	}
}
