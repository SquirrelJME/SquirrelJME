// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;

/**
 * This represents the base for an action which may be given a label, an
 * image, and could be enabled or disabled.
 *
 * @since 2018/03/31
 */
abstract class __Action__
	extends __Collectable__
{
	/** The short label. */
	volatile String _shortlabel;
	
	/** The long label. */
	volatile String _longlabel;
	
	/** The image used. */
	volatile Image _image;
	
	/** The font hint to use. */
	volatile Font _font;
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e If the parent is enabled or disabled.
	 * @since 2018/04/01
	 */
	abstract void onParentEnabled(boolean __e);
	
	/**
	 * Internally sets the labels to be displayed.
	 *
	 * @param __sl The short label.
	 * @param __ll The long label, {@code null} will clear it.
	 * @param __i The image to be displayed.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2018/03/29
	 */
	final void __setLabels(String __sl, String __ll, Image __i)
		throws NullPointerException
	{
		if (__sl == null)
			throw new NullPointerException("NARG");
		
		// Cache to prevent GC
		this._shortlabel = __sl;
		this._longlabel = __ll;
		this._image = __i;
		
		// Set remotely
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.ACTION_SET_LABELS,
			this._handle, __sl, __ll, (__i == null ? -1 : __i._handle));
	}
}

