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
 * This represents a class node which is represented by an input file.
 *
 * @since 2018/03/06
 */
public abstract class ClassNode
{
	/** The file used for input. */
	protected final CompilerInput input;
	
	/** The class this node provides information for. */
	protected final ClassName name;
	
	/**
	 * Initializes the base node.
	 *
	 * @param __name The name of the node.
	 * @param __input The input which contains the node data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	ClassNode(ClassName __name, CompilerInput __input)
		throws NullPointerException
	{
		if (__name == null || __input == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.input = __input;
	}
	
	/**
	 * Returns the compiler input used as the source for node.
	 *
	 * @return The compiler input used as the source.
	 * @since 2018/03/06
	 */
	public final CompilerInput compilerInput()
	{
		return this.input;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ClassNode))
			return false;
		
		return this.name.equals(((ClassNode)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the name of the node.
	 *
	 * @return The node name.
	 * @since 2018/03/06
	 */
	public final ClassName name()
	{
		return this.name;
	}
}

