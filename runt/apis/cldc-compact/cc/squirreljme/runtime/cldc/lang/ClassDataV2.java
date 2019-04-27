// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.asm.StaticMethod;

/**
 * This contains version 2 of the class data information.
 *
 * This does not extend {@link ClassDataV1} because it is intended to be
 * used by SummerCoat and RatufaCoat which are more native machines and such.
 *
 * @since 2019/04/26
 */
public class ClassDataV2
	extends ClassData
{
	/** Pointer to the class object. */
	public final int classobjptr;
	
	/**
	 * Version 2 constructor.
	 *
	 * @param __cop Pointer to the class object.
	 * @since 2019/04/26
	 */
	public ClassDataV2(int __cop)
	{
		super(2);
		
		// Set
		this.classobjptr = __cop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String binaryName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Class<?> component()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public int defaultConstructorFlags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public StaticMethod defaultConstructorMethod()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public int dimensions()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public StaticMethod enumValues()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public int flags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String inJar()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Class<?>[] interfaceClasses()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Class<?> superClass()
	{
		throw new todo.TODO();
	}
}

