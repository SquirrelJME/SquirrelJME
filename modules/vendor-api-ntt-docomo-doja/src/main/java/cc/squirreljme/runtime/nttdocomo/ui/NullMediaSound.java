// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.io.ConnectionException;
import com.nttdocomo.ui.MediaSound;
import com.nttdocomo.ui.UIException;

/**
 * A null sound which does nothing.
 *
 * @since 2024/01/14
 */
public class NullMediaSound
	implements MediaSound
{
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void dispose()
		throws UIException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void unuse()
		throws UIException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void use()
		throws ConnectionException, SecurityException, UIException
	{
		// Does nothing
	}
}
