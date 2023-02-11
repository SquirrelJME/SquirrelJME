// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.CompilationException;
import cc.squirreljme.jvm.aot.summercoat.pipe.SummerCoatClassLink;

/**
 * This is a handler which is capable of taking any input event created by
 * the processing of JARs into something that can be used by SummerCoat
 * layered compilers.
 *
 * @since 2022/09/02
 */
public interface SummerCoatHandler
	extends AutoCloseable
{
	/**
	 * Closes any streams or otherwise needed by the output.
	 * 
	 * @throws CompilationException If there were any compilation errors.
	 * @since 2022/09/07
	 */
	@Override
	void close()
		throws CompilationException;
	
	/**
	 * Returns the compiled class link for this handler.
	 * 
	 * @return The compiled class link.
	 * @since 2022/09/07
	 */
	SummerCoatClassLink compiledClassLink();
}
