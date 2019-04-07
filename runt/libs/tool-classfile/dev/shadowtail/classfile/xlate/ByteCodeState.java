// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.Instruction;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the state of the byte code.
 *
 * @since 2019/04/06
 */
public class ByteCodeState
{
	/** Java instruction. */
	public Instruction instruction;
	
	/** Simplified instruction. */
	public SimplifiedJavaInstruction simplified;
	
	/** The resulting stack. */
	public JavaStackState stack;
	
	/** The result of the operation. */
	public JavaStackResult result;
	
	/** The positions of all the stack information. */
	public Map<Integer, JavaStackState> stacks =
		new LinkedHashMap<>();
	
	/** The current source line being processed. */
	public int line;
	
	/** The current address being processed. */
	public int addr;
}

