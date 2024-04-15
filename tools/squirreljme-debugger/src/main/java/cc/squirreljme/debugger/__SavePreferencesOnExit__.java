// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.io.IOException;

/**
 * Saves preferences on exit.
 *
 * @since 2024/01/29
 */
class __SavePreferencesOnExit__
	implements Runnable
{
	/** The preference to save. */
	private final Preferences preferences;
	
	/**
	 * Saves preferences on exit.
	 *
	 * @param __preferences The preferences to save.
	 * @since 2024/01/29
	 */
	__SavePreferencesOnExit__(Preferences __preferences)
	{
		this.preferences = __preferences;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/29
	 */
	@Override
	public void run()
	{
		if (this.preferences != null)
		{
			PreferencesManager manager = new PreferencesManager();
			try
			{
				manager.store(this.preferences);
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
		}
	}
}
