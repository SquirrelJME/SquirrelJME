// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.c.CStructVariableBlock;
import java.io.IOException;

/**
 * This is the base interface for anything that represents a linkage.
 *
 * @since 2023/06/03
 */
public interface Linkage
{
	/**
	 * Writes to the output the C code to define the structure linkage.
	 *
	 * @param __output The output to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	void write(CStructVariableBlock __output)
		throws IOException, NullPointerException;
}
