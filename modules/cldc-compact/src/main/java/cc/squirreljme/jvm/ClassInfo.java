// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
@Deprecated
public final class ClassInfo
{
	/** The magic number which should be used. */
	public static final int MAGIC_NUMBER =
		0x4C6F7665;
	
	/** Self pointer. */
	public final int selfptr;
	
	/** Magic number used to detect corruption. */
	public final int magic;
	
	/** Class information flags. */
	public final int flags;
	
	/** The pointer to the minimized class file. */
	public final int miniptr;
	
	/** The pointer to the class name. */
	public final int namep;
	
	/** The allocation size of this class. */
	public final int size;
	
	/** The base offset for fields in this class. */
	public final int base;
	
	/** The number of objects in the instance fields, for GC. */
	public final int numobjects;
	
	/** The dimensions this class uses, if it is an array. */
	public final int dimensions;
	
	/** The cell size of components if this is an array. */
	public final int cellsize;
	
	/** The super class data. */
	public final ClassInfo superclass;
	
	/** Interfaces. */
	public final ClassInfo[] interfaceclasses;
	
	/** The component class. */
	public final ClassInfo componentclass;
	
	/** Pointer to the class object. */
	public final Class<?> classobjptr;
	
	/** Virtual invoke VTable. */
	public final int[] vtablevirtual;
	
	/** Virtual invoke VTable pool entries. */
	public final int[] vtablepool;
	
	/** The pointer to the constant pool of this class. */
	public final int pool;
	
	/** The JAR this class is a part of. */
	public final int jardx;
	
	/** The total method count. */
	public final int nummethods;
	
	/** The depth of this class. */
	public final int classdepth;
	
	/** The static field pointer offset for the class. */
	public final int sfoffset;
	
	/** The pointer of the default constructor. */
	public final int defaultnew;
	
	/**
	 * Class information constructor.
	 *
	 * @param __sp Self pointer.
	 * @param __fl Class information flags.
	 * @param __minip Pointer to the hardware class data in ROM.
	 * @param __namep The name pointer.
	 * @param __sz The size of this class.
	 * @param __bz The base offset for fields.
	 * @param __no The number of objects in the field instance.
	 * @param __dim Dimensions.
	 * @param __csz Cell size.
	 * @param __scl The super class data.
	 * @param __icl Interface classes.
	 * @param __ccl Component class.
	 * @param __cop Pointer to the class object.
	 * @param __vtv Virtual invoke VTable address.
	 * @param __vtp Virtual invoke VTable pool addresses.
	 * @param __pool The pointer to the class constant pool.
	 * @param __jardx The JAR Index.
	 * @param __nm The number of methods that are used.
	 * @param __cd The class depth.
	 * @param __sfp The static field offset.
	 * @param __dn The default constructor.
	 * @since 2019/04/26
	 */
	public ClassInfo(int __sp, int __fl, int __minip, int __namep, int __sz,
		int __bz, int __no, int __dim, int __csz, ClassInfo __scl,
		ClassInfo[] __icl, ClassInfo __ccl, Class<?> __cop, int[] __vtv,
		int[] __vtp, int __pool, int __jardx, int __nm, int __cd, int __sfp,
		int __dn)
	{
		// Always implicitly set magic
		this.magic = ClassInfo.MAGIC_NUMBER;
		
		// Set
		this.selfptr = __sp;
		this.flags = __fl;
		this.miniptr = __minip;
		this.namep = __namep;
		this.size = __sz;
		this.base = __bz;
		this.numobjects = __no;
		this.dimensions = __dim;
		this.cellsize = __csz;
		this.superclass = __scl;
		this.interfaceclasses = (__icl == null ? new ClassInfo[0] : __icl);
		this.componentclass = __ccl;
		this.classobjptr = __cop;
		this.vtablevirtual = (__vtv == null ? new int[0] : __vtv);
		this.vtablepool = (__vtp == null ? new int[0] : __vtp);
		this.pool = __pool;
		this.jardx = __jardx;
		this.nummethods = __nm;
		this.classdepth = __cd;
		this.sfoffset = __sfp;
		this.defaultnew = __dn;
	}
}

