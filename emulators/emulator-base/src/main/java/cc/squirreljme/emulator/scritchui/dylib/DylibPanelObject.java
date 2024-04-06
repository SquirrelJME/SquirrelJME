// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;

/**
 * A {@link ScritchPanelBracket}.
 *
 * @since 2024/04/06
 */
public class DylibPanelObject
	extends DylibBaseObject
	implements ScritchPanelBracket
{
	/**
	 * Initializes the panel object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/06
	 */
	public DylibPanelObject(long __objectP)
		throws NullPointerException
	{
		super(__objectP);
	}
}
