// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is the link glob for SummerCoat which stores the state of the compiler
 * as it processes multiple classes into one.
 *
 * @since 2022/09/04
 */
public final class SummerCoatLinkGlob
	implements LinkGlob
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public void finish()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public void join(String __name, boolean __isRc, InputStream __data)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
}
