// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.javac.cute.CuteCompilerService;

/**
 * This tests the cute compiler.
 *
 * @since 2019/06/30
 */
public class CuteCompilerTest
	extends __BaseCompiler__
{
	/**
	 * Tests the cute compiler.
	 *
	 * @since 2019/06/30
	 */
	public CuteCompilerTest()
	{
		super(new CuteCompilerService());
	}
}

