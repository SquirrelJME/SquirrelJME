// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import net.multiphasicapps.tac.TestBoolean;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests that the application list has applications on it, that all the
 * applications could be properly loaded and whatnot.
 *
 * @since 2023/02/11
 */
public class TestHasApps
	extends TestBoolean
{
	/**
	 * {@inheritDoc}
	 * @since 2023/02/11
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	public boolean test()
	{
		// Check for native display
		UIDisplayBracket[] displays = UIFormShelf.displays();
		if (displays == null || displays.length == 0 ||
			0 == UIFormShelf.metric(displays[0],
				UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException("Native forms not supported.");
		
		MidletMain main = new MidletMain();
		
		// Perform refresh
		main.refresh(null);
		
		// Wait until refreshing is done
		for (;;)
			synchronized (main)
			{
				if (!main._refreshLock)
					break;
			}
		
		// This must have stuff inside of it
		return !main._listedSuites.isEmpty();
	}
}
