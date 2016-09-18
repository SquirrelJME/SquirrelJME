// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import java.io.IOException;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.CompilerOutput;

/**
 * This bridges Java SE's compiler to the SquirrelJME bootstrap system.
 *
 * @since 2016/09/18
 */
public class BridgedJavaCompiler
	implements BootCompiler
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public boolean compile(CompilerOutput __co)
		throws IOException
	{
		throw new Error("TODO");
	}
}

