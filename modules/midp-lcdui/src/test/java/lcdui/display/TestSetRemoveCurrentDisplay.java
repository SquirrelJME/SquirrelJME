// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.display;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import lcdui.BaseDisplay;
import net.multiphasicapps.tac.OptionalFirstParameter;

/**
 * Tests that setting and removing the current display is valid.
 *
 * @since 2020/07/26
 */
public class TestSetRemoveCurrentDisplay
	extends BaseDisplay
	implements OptionalFirstParameter
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public void test(Display __display, String __param)
	{
		// Use any kind of thing
		Displayable form = new Form(null);
		
		// Should not be our current item
		this.secondary("not-form", form != __display.getCurrent());
		
		// Should be our set form
		__display.setCurrent(form);
		this.secondary("after-set", form == __display.getCurrent());
		
		// Remove the form, which should result in null
		__display.removeCurrent();
		this.secondary("is-null", null == __display.getCurrent());
	}
}
