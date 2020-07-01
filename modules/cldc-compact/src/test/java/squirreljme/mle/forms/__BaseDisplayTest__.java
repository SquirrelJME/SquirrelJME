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
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;
import net.multiphasicapps.tac.UntestableException;

/**
 * This is the base class for form tests, this will check that UIForms are
 * supported before running them.
 * 
 * All of the sub-classes should test {@link UIFormShelf}.
 *
 * @since 2020/07/01
 */
abstract class __BaseDisplayTest__
	extends TestRunnable
{
	/**
	 * Performs the UI test.
	 * 
	 * @throws Throwable Any exceptions as needed.
	 * @since 2020/07/01
	 */
	protected abstract void uiTest(UIDisplayBracket __display)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public final void test()
		throws Throwable
	{
		// Test guard
		if (0 == UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException("UIForms not supported.");
		
		// Make sure there is at least one display
		UIDisplayBracket[] displays;
		try
		{
			displays = UIFormShelf.displays();
			if (displays.length == 0)
				throw new UntestableException("No displays."); 
		}
		catch (MLECallError e)
		{
			throw new UntestableException("No displays.", e);
		}
		
		// Run the actual test now
		this.uiTest(displays[0]);
	}
}
