// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes ASCII85.
 *
 * @since 2024/05/17
 */
public class ASCII85Decoder
	extends InputStream
{
	/**
	 * {@inheritDoc}
	 * @since 2024/05/17
	 */
	@Override
	public int read()
		throws IOException
	{
		throw Debugging.todo();
	}
}
