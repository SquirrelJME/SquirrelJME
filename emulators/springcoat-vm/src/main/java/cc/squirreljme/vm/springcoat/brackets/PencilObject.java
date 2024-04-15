// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
import javax.microedition.lcdui.Graphics;

/**
 * Represents {@link PencilBracket}.
 *
 * @since 2021/12/05
 */
public class PencilObject
	extends AbstractGhostObject
{
	/** The graphics to draw with. */
	public final Graphics graphics;
	
	/**
	 * Initializes the graphics.
	 *
	 * @param __machine The machine used.
	 * @param __g The graphics to draw,
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	public PencilObject(SpringMachine __machine, Graphics __g)
		throws NullPointerException
	{
		super(__machine, PencilBracket.class);
		
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this.graphics = __g;
	}
}


