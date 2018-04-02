// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents an action which has a label, image, and may be potentially
 * enabled or disabled.
 *
 * @since 2018/03/31
 */
public abstract class LcdAction
	extends LcdCollectable
{
	/** The label. */
	private String _label;
	
	/** The long label. */
	private String _longlabel;
	
	/** The image used. */
	private LcdImage _image;
	
	/**
	 * Initializes the base action.
	 *
	 * @param __handle The handle of the command.
	 * @param __type The type of collectable this is.
	 * @since 2018/03/31
	 */
	public LcdAction(int __handle, CollectableType __type)
	{
		super(__handle, __type);
	}
	
	/**
	 * Sets the labels for the action.
	 *
	 * @param __sl The short label, must always be set.
	 * @param __ll The long label.
	 * @param __i The image to show.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2018/04/02
	 */
	public final void setLabels(String __sl, String __ll, LcdImage __i)
		throws NullPointerException
	{
		if (__sl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._label = __sl;
		this._longlabel = __ll;
		this._image = __i;
		
		// Note
		todo.TODO.note("Actually do something after labels are set!");
	}
}

