// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.classfile.BinaryName;

/**
 * This is a symbol which represents the name of a class.
 *
 * @since 2018/05/05
 */
@Deprecated
public class ClassSymbol
	implements StructureSymbol
{
	/** The binary name of the class. */
	protected final BinaryName binaryname;
	
	/**
	 * Initializes the class symbol.
	 *
	 * @param __bn The binary name to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public ClassSymbol(BinaryName __bn)
		throws NullPointerException
	{
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		this.binaryname = __bn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Translates this class symbol to the name of the class file this will
	 * belong to.
	 *
	 * @return The class file name.
	 * @since 2018/05/05
	 */
	public final String toClassFileName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Translates this class symbol to the name of the source code files that
	 * may be used for this class, this results in multiple files potentially
	 * being produced as there may be unknown sub-classes.
	 *
	 * @return The source file names used for this class.
	 * @since 2018/05/05
	 */
	public final String[] toSourceFileNames()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the given file name to the class symbol it should be
	 * associated with.
	 *
	 * @param __fn The filename.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public static ClassSymbol fromSourceFileName(String __fn)
		throws NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

