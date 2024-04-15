// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.lists;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.lcdui.font.FontUtilities;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import javax.microedition.lcdui.Font;

/**
 * Tests resetting of list items after a list has been resized and those items
 * were there. New items should be initialized to zero, while items that get
 * erased away should revert back to zero.
 * 
 * The end result of the test should be that items that get added are properly
 * cleared without affecting lower items.
 *
 * @since 2020/10/18
 */
public class TestListItemReset
	extends BaseList
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
	{
		ListItem zeroItem = new ListItem();
		
		// Newly added items are always zero!
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 1);
		ListItem first = ListItem.of(__backend, __list, 0);
		
		this.secondary("first-zero", zeroItem.equals(first));
		
		// If we modify the first item, we should be able to read everything
		// back just fine and it should be the same!
		first._idCode = 0x1234_5678;
		first._iconDimension = 16;
		first._selected = false;
		first._disabled = true;
		first._font = FontUtilities.fontToSystemFont(Font.getDefaultFont());
		first._label = "Cute Squirrel!";
		
		first.into(__backend, __list, 0);
		ListItem storedFirst = ListItem.of(__backend, __list, 0);
		
		this.secondary("first-set", first.equals(storedFirst));
		
		// Adding one more entry to the list should make the first entry keep
		// the same information
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 2);
		ListItem firstPrime = ListItem.of(__backend, __list, 0);
		
		this.secondary("first-prime", first.equals(firstPrime));
		
		// The second item should be blank!
		ListItem second = ListItem.of(__backend, __list, 1);
		
		this.secondary("second-zero", zeroItem.equals(second));
		
		// Modifying the second item should keep the first the same but have
		// the second be as stored
		second._idCode = 0x1834_5678;
		second._iconDimension = 32;
		second._selected = true;
		second._disabled = false;
		second._font = FontUtilities.fontToSystemFont(Font.getFont(
			Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		second._label = "Cute Squeak!";
		
		second.into(__backend, __list, 1);
		ListItem storedSecond = ListItem.of(__backend, __list, 1);
		ListItem firstly = ListItem.of(__backend, __list, 0);
		
		this.secondary("firstly", first.equals(firstly));
		this.secondary("second-set", second.equals(storedSecond));
		
		// Shrinking the list, the first item should be safe
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 1);
		ListItem firstlyAgain = ListItem.of(__backend, __list, 0);
		
		this.secondary("firstly-again", first.equals(firstlyAgain));
		
		// Adding the second item slot back should have erased the old one to
		// zero
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 2);
		ListItem secondZero = ListItem.of(__backend, __list, 1);
		
		this.secondary("second-last-zero", zeroItem.equals(secondZero));
	}
}
