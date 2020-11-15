// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;

/**
 * This is the base class that is under various widgets.
 *
 * @since 2020/10/17
 */
abstract class __CommonWidget__
	implements DisplayWidget
{
	/**
	 * Is this item painted?
	 * 
	 * @return If this can be painted on.
	 * @since 2020/10/17
	 */
	boolean __isPainted()
	{
		return false;
	}
	
	/**
	 * Paints and forwards Graphics.
	 * 
	 * @param __gfx Graphics to draw.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special The special painting code, may be {@code 0} or any
	 * other value depending on what is being painted.
	 * @since 2020/09/21
	 */
	void __paint(Graphics __gfx, int __sw, int __sh, int __special)
	{
	}
	
	/**
	 * Handles property changes.
	 * 
	 * @param __form The form this affects.
	 * @param __item The item this affects.
	 * @param __intProp The property that changed.
	 * @param __sub The sub-index.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @return If the event was handled and we should stop.
	 * @since 2020/10/17
	 */
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		// Fallback to sending to the item
		DisplayWidget item = StaticDisplayState.locate(__item);
		if (item instanceof __CommonWidget__)
			return ((__CommonWidget__)item).__propertyChange(__form, __item,
				__intProp, __sub, __old, __new);
		
		// Un-Handled
		return false;
	}
	
	/**
	 * Handles property changes.
	 * 
	 * @param __form The form this affects.
	 * @param __item The item this affects.
	 * @param __strProp The property that changed.
	 * @param __sub The sub-index.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @return If the event was handled and we should stop.
	 * @since 2020/10/17
	 */
	boolean __propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		// Fallback to sending to the item, only if the specified item is not
		// registered to us as it would have been handled alreayd
		DisplayWidget item = StaticDisplayState.locate(__item);
		if (item instanceof __CommonWidget__ && item != this)
			return ((__CommonWidget__)item).__propertyChange(__form, __item,
				__strProp, __sub, __old, __new);
		
		// Un-Handled
		return false;
	}
}
