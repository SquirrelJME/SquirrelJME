// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;

/**
 * Wraps a native {@link UIDisplayBracket}.
 *
 * @since 2020/07/01
 */
public final class UIDisplayObject
	extends AbstractGhostObject
{
	/** The display to wrap. */
	public final UIDisplayBracket display;
	
	/**
	 * Initializes the display object.
	 * 
	 * @param __display The display to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public UIDisplayObject(UIDisplayBracket __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
	}
}
