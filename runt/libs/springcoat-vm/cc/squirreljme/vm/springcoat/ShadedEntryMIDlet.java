// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is the main entry point for MIDlets to the VM.
 *
 * @since 2018/11/16
 */
public class ShadedEntryMIDlet
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/16
	 */
	@Override
	protected final void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		// Not used
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/16
	 */
	@Override
	protected final void startApp()
		throws MIDletStateChangeException
	{
		ShadedMain.shadedMain();
	}
}

