// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerService;

/**
 * This creates instances of the cute compiler.
 *
 * @since 2019/06/30
 */
public final class CuteCompilerService
	implements CompilerService
{
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final Compiler createInstance()
	{
		return new CuteCompiler();
	}
}

