// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuKindBracket;

/**
 * Represents the menu bar.
 *
 * @since 2024/07/20
 */
public class DylibMenuBarObject
	extends DylibBaseObject
	implements ScritchMenuBarBracket, DylibMenuKindObject,
		ScritchMenuKindBracket
{
	/**
	 * Initializes the menu bar object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public DylibMenuBarObject(long __objectP)
	{
		super(__objectP);
	}
}
