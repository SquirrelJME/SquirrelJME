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
 * VTables just reference only instance methods and similarly to instance
 * fields, their IDs and tables build upon each other. For example if Class A
 * contains 5 methods and Class B contains 7 methods, then Class A will have
 * a VTable size of 5 while Class B will have a VTable size of 12.
 *
 * @since 2019/04/26
 */
@Deprecated
public class ClassDataV2
	extends ClassData
{
	/** The magic number which should be used. */
	public static final int MAGIC_NUMBER =
		0x4C6F7665;
	
	/** Magic number used to detect corruption. */
	public final int magic;
	
	/** The pointer to the minimized class file. */
	public final int miniptr;
	
	/** The allocation size of this class. */
	public final short size;
	
	/** The base offset for fields in this class. */
	public final short base;
	
	/** The dimensions this class uses, if it is an array. */
	public final byte dimensions;
	
	/** The cell size of components if this is an array. */
	public final short cellsize;
	
	/** The super class data. */
	public final int superclass;
	
	/** Pointer to the class object. */
	public final int classobjptr;
	
	/** Virtual invoke VTable. */
	public final int vtablevirtual;
	
	/** Special invoke VTable. */
	public final int vtablespecial;
	
	/**
	 * Version 2 constructor.
	 *
	 * @param __minip Pointer to the hardware class data in ROM.
	 * @param __sz The size of this class.
	 * @param __bz The base offset for fields.
	 * @param __dim Dimensions.
	 * @param __csz Cell size.
	 * @param __scl The super class data.
	 * @param __cop Pointer to the class object.
	 * @param __vtv Virtual invoke VTable address.
	 * @param __vts Special invoke VTable address.
	 * @since 2019/04/26
	 */
	public ClassDataV2(int __minip, short __sz, short __bz,
		byte __dim, short __csz, int __scl, int __cop, int __vtv, int __vts)
	{
		super(2);
		
		// Always implicitly set magic
		this.magic = MAGIC_NUMBER;
		
		// Set
		this.miniptr = __minip;
		this.size = __sz;
		this.base = __bz;
		this.dimensions = __dim;
		this.cellsize = __csz;
		this.superclass = __scl;
		this.classobjptr = __cop;
		this.vtablevirtual = __vtv;
		this.vtablespecial = __vts;
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

