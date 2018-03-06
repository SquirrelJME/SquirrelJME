// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.javac.CompilerInput;

/**
 * This is a class node which is based on input source code and is compiled
 * by the compiler as it contains instructions to generate.
 *
 * @since 2018/03/06
 */
public final class SourcedClassNode
	extends ClassNode
{
	/**
	 * Initializes a node which has semi-parsed structure information and
	 * builds a remaining structure from it.
	 *
	 * @param __name The name of the node.
	 * @param __input The input which contains the node data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	private SourcedClassNode(ClassName __name, CompilerInput __input)
		throws NullPointerException
	{
		super(__name, __input);
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the input source file and generates nodes and adds them to the
	 * compiler state, the parsed nodes contain incompletely compiled class
	 * file data.
	 *
	 * @param __input The input which contains the node data.
	 * @param __state The compiler state which is used to initialize more
	 * nodes.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	static final void __loadNodes(CompilerInput __input, CompilerState __state)
		throws NullPointerException
	{
		if (__input == null || __state == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ0s Parsing input source file for classes to
		// compile.}
		__state.message(MessageType.INFO, __input, "AQ0s");
		
		throw new todo.TODO();
	}
}

