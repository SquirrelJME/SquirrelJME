// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.ZipCompilerOutput;

/**
 * Palm OS distribution.
 *
 * @since 2019/07/13
 */
public class PalmOSDist
	extends SummerCoatROM
{
	/**
	 * Initializes the builder.
	 *
	 * @since 2019/07/13
	 */
	public PalmOSDist()
	{
		super("palmos");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	protected void generate(ZipCompilerOutput __zip, BuildParameters __bp)
		throws IOException, NullPointerException
	{
		if (__zip == null || __bp == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

