// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;

/**
 * Base for components.
 *
 * @since 2024/04/18
 */
public abstract class DylibComponentObject
	extends DylibBaseObject
	implements ScritchComponentBracket
{
	/**
	 * Initializes the base object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/04
	 */
	public DylibComponentObject(long __objectP)
		throws NullPointerException
	{
		super(__objectP);
	}
}
