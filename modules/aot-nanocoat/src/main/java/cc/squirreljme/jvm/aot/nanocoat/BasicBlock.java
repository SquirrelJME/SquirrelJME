// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.List;
import net.multiphasicapps.classfile.Instruction;

/**
 * Basic block within the byte code.
 *
 * @since 2023/05/31
 */
public class BasicBlock
{
	/**
	 * Returns the instructions within the block.
	 * 
	 * @return The instructions within the block.
	 * @since 2023/05/31
	 */
	public List<Instruction> instructions()
	{
		throw Debugging.todo();
	}
}
