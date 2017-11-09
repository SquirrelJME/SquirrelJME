// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.unsafe.SystemFile;

/**
 * This wraps the standard default output stream.
 *
 * @since 2016/06/16
 */
final class __StandardOutput__
	extends OutputStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/16
	 */
	@Override
	public void write(int __b)
	{
		SystemFile.stdOut(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
	{
		SystemFile.stdOut(__b, __o, __l);
	}
}

