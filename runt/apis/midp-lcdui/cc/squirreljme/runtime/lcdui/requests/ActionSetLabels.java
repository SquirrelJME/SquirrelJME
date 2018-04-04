// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.requests;

import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdAction;
import cc.squirreljme.runtime.lcdui.server.LcdImage;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;

/**
 * This sets the labels and images for an action.
 *
 * @since 2018/04/02
 */
public final class ActionSetLabels
	extends LcdRequest
{
	/** The action to set. */
	protected final LcdAction action;
	
	/** The short label to set. */
	protected final String shortlabel;
	
	/** The long label to set. */
	protected final String longlabel;
	
	/** The image to display. */
	protected final LcdImage image;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __a The action to modify.
	 * @param __sl The short label.
	 * @param __ll The long label.
	 * @param __i The image.
	 * @throws NullPointerException If {@code __sv}, {@code __a}, or
	 * {@code __sl} are {@code null}.
	 * @since 2018/04/02
	 */
	public ActionSetLabels(LcdServer __sv, LcdAction __a, String __sl,
		String __ll, LcdImage __i)
		throws NullPointerException
	{
		super(__sv, LcdFunction.ACTION_SET_LABELS);
		
		if (__a == null || __sl == null)
			throw new NullPointerException("NARG");
		
		this.action = __a;
		this.shortlabel = __sl;
		this.longlabel = __ll;
		this.image = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	protected final Object invoke()
	{
		this.action.setLabels(this.shortlabel, this.longlabel, this.image);
		return VoidType.INSTANCE;
	}
}

