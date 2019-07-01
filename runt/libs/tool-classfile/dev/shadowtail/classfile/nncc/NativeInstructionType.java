// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

/**
 * This represents a type of native instruction. Virtually all instructions
 * except for the special instructions are composed of groups.
 *
 * @since 2019/04/06
 */
public interface NativeInstructionType
{
	/**
	 * Math, R=RR, Integer.
	 * {@code iiiiMMMM, r3 = r1 ? r2}.
	 */
	public static final short MATH_REG_INT =
		0x00;
	
	/**
	 * Int comparison, then maybe jump.
	 * {@code iiiiRccc, if (r1 ? r2) jump->j3}.
	 */
	public static final short IF_ICMP =
		0x10;
	
	/**
	 * Memory access, offset is in register.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final short MEMORY_OFF_REG =
		0x20;
	
	/**
	 * Memory access to big endian Java format, offset is in register.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final short MEMORY_OFF_REG_JAVA =
		0x30;
	
	/**
	 * Math, R=RC, Integer.
	 * {@code iiiiMMMM, r3 = r1 ? c2}.
	 */
	public static final short MATH_CONST_INT =
		0x80;
	
	/**
	 * Memory access, offset is a constant.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final short MEMORY_OFF_ICONST =
		0xA0;
	
	/**
	 * Memory access to big endian Java format, offset is a constant.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final short MEMORY_OFF_ICONST_JAVA =
		0xB0;
	
	/**
	 * Special.
	 * {@code iiiixxxx}.
	 */
	public static final short SPECIAL_A =
		0xE0;
	
	/**
	 * Special.
	 * {@code iiiixxxx}.
	 */
	public static final short SPECIAL_B =
		0xF0;
	
	/** If equal to constant. */
	public static final short IFEQ_CONST =
		0xE6;
	
	/** Debug entry to method. */
	public static final short DEBUG_ENTRY =
		0xE8;
	
	/** Debug exit from method. */
	public static final short DEBUG_EXIT =
		0xE9;
	
	/** Debug single point in method. */
	public static final short DEBUG_POINT =
		0xEA;
	
	/**
	 * Return. 
	 * {@code iiiixxxx, return}.
	 */
	public static final short RETURN =
		0xF3;
	
	/**
	 * Invoke. 
	 * {@code iiiixxxx}.
	 */
	public static final short INVOKE =
		0xF7;
	
	/** Copy value in register. */
	public static final short COPY =
		0xF8;
	
	/** Atomically decrements a memory addres and gets the value. */
	public static final short ATOMIC_INT_DECREMENT_AND_GET =
		0xF9;
	
	/** Atomically increments a memory address. */
	public static final short ATOMIC_INT_INCREMENT =
		0xFA;
	
	/** System call. */
	public static final short SYSTEM_CALL =
		0xFB;
	
	/** Atomic compare and set. */
	public static final short ATOMIC_COMPARE_GET_AND_SET =
		0xFC;
	
	/**
	 * Load from pool, note that at code gen time this is aliased.
	 * {@code iiiixxxx}.
	 */
	public static final short LOAD_POOL =
		0xFD;
	
	/** Load from integer array. */
	public static final short LOAD_FROM_INTARRAY =
		0xFE;
	
	/**
	 * Compare and exchange. 
	 * {@code iiiixxxx}.
	 */
	public static final short BREAKPOINT =
		0xFF;
}

