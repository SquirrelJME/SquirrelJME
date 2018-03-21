// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import net.multiphasicapps.classfile.BinaryName;

/**
 * This represents a basic class in the source code.
 *
 * @since 2018/03/21
 */
public final class BasicClassBuilder
{
	/** The flags for the class. */
	volatile DefinedClassFlags _flags;
	
	/** The name of this class. */
	volatile BinaryName _name;
	
	/**
	 * Adds an extend.
	 *
	 * @param __bn The class to extend.
	 * @return {@code this}.
	 * @throws BasicStructureException If the extend has been duplicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public final BasicClassBuilder addExtends(BinaryName __bn)
		throws BasicStructureException, NullPointerException
	{
		if (__bn == null)
			throw new BasicStructureException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Adds an implement.
	 *
	 * @param __bn The class to implement.
	 * @return {@code this}.
	 * @throws BasicStructureException If the implement has been duplicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public final BasicClassBuilder addImplements(BinaryName __bn)
		throws BasicStructureException, NullPointerException
	{
		if (__bn == null)
			throw new BasicStructureException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This is used to construct a class.
	 *
	 * @since 2018/03/21
	 */
	public final BasicClass build()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the flags for the class.
	 *
	 * @param __f The class flags.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public final BasicClassBuilder flags(DefinedClassFlags __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		this._flags = __f;
		
		return this;
	}
	
	/**
	 * Sets the name for the class.
	 *
	 * @param __n The class name.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public final BasicClassBuilder name(BinaryName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this._name = __n;
		
		return this;
	}
}

