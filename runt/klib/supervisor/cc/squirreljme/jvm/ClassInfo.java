// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This contains class information which is used to define the information
 * that is needed to parse it properly.
 *
 * @since 2019/05/25
 */
public final class ClassInfo
{
	/** The magic number which should be used. */
	public static final int MAGIC_NUMBER =
		0x4C6F7665;
	
	/** Magic number used to detect corruption. */
	public final int magic;
	
	/** The pointer to the minimized class file. */
	public final int miniptr;
	
	/** The allocation size of this class. */
	public final int size;
	
	/** The base offset for fields in this class. */
	public final int base;
	
	/** The dimensions this class uses, if it is an array. */
	public final int dimensions;
	
	/** The cell size of components if this is an array. */
	public final int cellsize;
	
	/** The super class data. */
	public final ClassInfo superclass;
	
	/** Pointer to the class object. */
	public final Class<?> classobjptr;
	
	/** Virtual invoke VTable. */
	public final int[] vtablevirtual;
	
	/** Virtual invoke VTable pool entries. */
	public final int[] vtablepool;
	
	/**
	 * Class information constructor.
	 *
	 * @param __minip Pointer to the hardware class data in ROM.
	 * @param __sz The size of this class.
	 * @param __bz The base offset for fields.
	 * @param __dim Dimensions.
	 * @param __csz Cell size.
	 * @param __scl The super class data.
	 * @param __cop Pointer to the class object.
	 * @param __vtv Virtual invoke VTable address.
	 * @param __vtp Virtual invoke VTable pool addresses.
	 * @since 2019/04/26
	 */
	public ClassInfo(int __minip, int __sz, int __bz, int __dim, int __csz,
		ClassInfo __scl, Class<?> __cop, int[] __vtv, int[] __vtp)
	{
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
		this.vtablepool = __vtp;
	}
}

