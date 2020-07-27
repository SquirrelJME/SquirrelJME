// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

/**
 * Base class for form related tests.
 *
 * @since 2020/07/26
 */
public abstract class BaseForm
	extends BaseDisplay
{
	/**
	 * Tests on the given form.
	 * 
	 * @param __display The display being tested.
	 * @param __form The form to test on.
	 * @throws Throwable On any exception.
	 * @since 2020/07/26
	 */
	protected abstract void test(Display __display, Form __form)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/27
	 */
	@Override
	public final void test(Display __display)
		throws Throwable
	{
		Form form = new Form("Test");
		
		__display.setCurrent(form);
		
		this.test(__display, form);
	}
}
