// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;

/**
 * Interface that is used a callback on a user-interface form is to be done.
 *
 * @since 2020/07/03
 */
public interface UIFormCallback
	extends ShelfCallback, UIDrawableCallback
{
	/**
	 * Indicates that a form is being refreshed before items are about to be
	 * placed onto the form.
	 * 
	 * If the size of the form is unknown, then {@code -1} should be used for
	 * the form properties. Note that the form size is only an estimate and
	 * might not be accurate.
	 * 
	 * @param __form The form being refreshed.
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @since 2022/07/20
	 */
	void formRefresh(UIFormBracket __form, int __sx, int __sy,
		int __sw, int __sh);
	
	/**
	 * This is called when a property on an item has changed.
	 * 
	 * @param __form The form to be acted on.
	 * @param __item The item to be acted on.
	 * @param __intProp One of {@link UIWidgetProperty}.
	 * @param __sub The sub-index of the property.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @since 2020/07/19
	 */
	void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new);
	
	/**
	 * This is called when a property on an item has changed.
	 * 
	 * @param __form The form to be acted on.
	 * @param __item The item to be acted on.
	 * @param __strProp One of {@link UIWidgetProperty}.
	 * @param __sub The sub-index of the property.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @since 2020/07/19
	 */
	void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new);
}
