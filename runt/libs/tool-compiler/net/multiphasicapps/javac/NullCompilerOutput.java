// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.NullOutputStream;

/**
 * This is a compiler output which writes to nowhere.
 *
 * @since 2019/06/30
 */
public final class NullCompilerOutput
	implements CompilerOutput
{
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void flush()
		throws CompilerException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final OutputStream output(String __n)
		throws CompilerException, NullPointerException
	{
		return new NullOutputStream();
	}
}

