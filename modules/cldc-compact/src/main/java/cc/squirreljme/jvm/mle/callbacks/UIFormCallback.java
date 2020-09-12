// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemIntegerProperty;
import cc.squirreljme.jvm.mle.constants.UIItemStringProperty;
import cc.squirreljme.jvm.mle.constants.UIKeyEventType;
import cc.squirreljme.jvm.mle.constants.UIKeyModifier;
import cc.squirreljme.jvm.mle.constants.UIMouseButton;
import cc.squirreljme.jvm.mle.constants.UIMouseEventType;

/**
 * Interface that is used a callback on a user-interface form is to be done.
 *
 * @since 2020/07/03
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface UIFormCallback
	extends ShelfCallback
{
	/**
	 * This is called on a keyboard/joystick action.
	 * 
	 * @param __form The form acted on.
	 * @param __item The item acted on.
	 * @param __event One of {@link UIKeyEventType}.
	 * @param __keyCode Key code for the event.
	 * @param __modifiers Bit mask of {@link UIKeyModifier}.
	 * @since 2020/07/19
	 */
	void eventKey(UIFormBracket __form, UIItemBracket __item, int __event,
		int __keyCode, int __modifiers);
	
	/**
	 * This is called on a mouse action.
	 * 
	 * @param __form The form acted on.
	 * @param __item The item acted on.
	 * @param __event One of {@link UIMouseEventType}.
	 * @param __button One of {@link UIMouseButton}.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __modifiers Bit mask of {@link UIKeyModifier}.
	 * @since 2020/07/19
	 */
	void eventMouse(UIFormBracket __form, UIItemBracket __item, int __event,
		int __button, int __x, int __y, int __modifiers);
	
	/**
	 * Request to exit the form, usually means the application is being
	 * closed or attempted to be closed.
	 * 
	 * @param __form The form being exited.
	 * @since 2020/09/12
	 */
	void exitRequest(UIFormBracket __form);
	
	/**
	 * Draw action for the given item.
	 * 
	 * @param __form The form to be acted on.
	 * @param __item The item to draw.
	 * @since 2020/07/19
	 */
	void paint(UIFormBracket __form, UIItemBracket __item);
	
	/**
	 * This is called when a property on an item has changed.
	 * 
	 * @param __form The form to be acted on.
	 * @param __item The item to be acted on.
	 * @param __intProp One of {@link UIItemIntegerProperty}.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @since 2020/07/19
	 */
	void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __old, int __new);
	
	/**
	 * This is called when a property on an item has changed.
	 * 
	 * @param __form The form to be acted on.
	 * @param __item The item to be acted on.
	 * @param __strProp One of {@link UIItemStringProperty}.
	 * @param __old The old value.
	 * @param __new The new value.
	 * @since 2020/07/19
	 */
	void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, String __old, String __new);
}
