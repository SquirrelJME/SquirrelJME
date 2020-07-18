// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.forms;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;

/**
 * Base tests on forms associated with a display.
 *
 * @since 2020/07/01
 */
abstract class __BaseFormTest__
	extends __BaseDisplayTest__
{
	/**
	 * Performs the UI test.
	 * 
	 * @param __display The display.
	 * @param __form The form.
	 * @throws Throwable Any exceptions as needed.
	 * @since 2020/07/01
	 */
	protected abstract void uiTest(UIDisplayBracket __display,
		UIFormBracket __form)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	protected final void uiTest(UIDisplayBracket __display)
		throws Throwable
	{
		UIFormBracket form = UIFormShelf.formNew();
		try
		{
			// We should actually display the form in order to see if it
			// works
			UIFormShelf.displayShow(__display, form);
			
			// Run the test
			this.uiTest(__display, form);
			
			// Sleep a bit to show the screen
			try
			{
				Thread.sleep(750);
			}
			catch (InterruptedException ignored)
			{
			}
		}
		
		// Delete the form
		finally
		{
			UIFormShelf.formDelete(form);
		}
	}
}
