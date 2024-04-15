// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

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
	 * @param __machine The machine used.
	 * @param __display The display to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public UIDisplayObject(SpringMachine __machine, UIDisplayBracket __display)
		throws NullPointerException
	{
		super(__machine, UIDisplayBracket.class);
		
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
	}
}
