// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;

/**
 * Represents the menu item.
 *
 * @since 2024/07/21
 */
public class DylibMenuItemObject
	extends DylibBaseObject
	implements ScritchMenuItemBracket, DylibLabelObject, DylibMenuKindObject
{
	/**
	 * Initializes the menu item object.
	 *
	 * @param __objectP The object pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public DylibMenuItemObject(long __objectP)
	{
		super(__objectP);
	}
}
