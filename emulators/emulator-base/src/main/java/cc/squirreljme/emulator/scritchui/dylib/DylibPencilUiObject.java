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

/**
 * Dynamic library based pencil drawing state.
 *
 * @since 2024/05/04
 */
public class DylibPencilUiObject
	extends DylibBaseObject
	implements DylibPencilObject
{
	/**
	 * Initializes the pencil object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/04
	 */
	public DylibPencilUiObject(long __objectP)
		throws NullPointerException
	{
		super(__objectP);
	}
}
