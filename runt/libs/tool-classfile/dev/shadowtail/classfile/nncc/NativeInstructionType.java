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
	public static final int MATH_REG_INT =
		0x00;
	
	/**
	 * Math, R=RR, Float.
	 * {@code iiiiMMMM, r3 = r1 ? r2}.
	 */
	public static final int MATH_REG_FLOAT =
		0x10;
	
	/**
	 * Math, R=RR, Long.
	 * {@code iiiiMMMM, r3 = r1 ? r2}.
	 */
	public static final int MATH_REG_LONG =
		0x20;
	
	/**
	 * Math, R=RR, Double.
	 * {@code iiiiMMMM, r3 = r1 ? r2}.
	 */
	public static final int MATH_REG_DOUBLE =
		0x30;
	
	/**
	 * Int comparison, then maybe jump.
	 * {@code iiiiRccc, if (r1 ? r2) jump->j3}.
	 */
	public static final int IF_ICMP =
		0x40;
	
	/**
	 * Memory access, offset is in register.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final int MEMORY_OFF_REG =
		0x50;
	
	/** Memory access, off a register using long value. */
	public static final int MEMORY_OFF_REG_WIDE =
		0x57;
	
	/** Wide array access. */
	public static final int ARRAY_ACCESS_WIDE =
		0b0111_1100;
	
	/**
	 * Math, R=RC, Integer.
	 * {@code iiiiMMMM, r3 = r1 ? c2}.
	 */
	public static final int MATH_CONST_INT =
		0x80;
	
	/**
	 * Math, R=RC, Float.
	 * {@code iiiiMMMM, r3 = r1 ? c2}.
	 */
	public static final int MATH_CONST_FLOAT =
		0x90;
	
	/**
	 * Math, R=RC, Long.
	 * {@code iiiiMMMM, r3 = r1 ? c2}.
	 */
	public static final int MATH_CONST_LONG =
		0xA0;
	
	/**
	 * Math, R=RC, Double.
	 * {@code iiiiMMMM, r3 = r1 ? c2}.
	 */
	public static final int MATH_CONST_DOUBLE =
		0xB0;
	
	/**
	 * Conversion (narrow to narrow).
	 * {@code iiiiFFTT, r2 <= r1}.
	 */
	public static final int CONVERSION =
		0b1100_00_00;
	
	/**
	 * Conversion (narrow to wide).
	 * {@code iiiiFFTT, r2 <= r1}.
	 */
	public static final int CONVERSION_TO_WIDE =
		0b1100_00_01;
	
	/**
	 * Conversion (wide to narrow).
	 * {@code iiiiFFTT, r2 <= r1}.
	 */
	public static final int CONVERSION_FROM_WIDE =
		0b1100_01_00;
	
	/**
	 * Conversion (wide to wide).
	 * {@code iiiiFFTT, r2 <= r1}.
	 */
	public static final int CONVERSION_WIDE =
		0b1100_01_01;
	
	/**
	 * Memory access, offset is a constant.
	 * {@code iiiiLddd, L=T load r1 = *(r2 + r3), L=F set *(r2 + r3) = r1}.
	 */
	public static final int MEMORY_OFF_ICONST =
		0xD0;
	
	/** Memory access, offset is a constant, long value. */
	public static final int MEMORY_OFF_ICONST_WIDE =
		0xD7;
	
	/**
	 * Unused.
	 * {@code iiii????}.
	 */
	public static final int SPECIAL_A =
		0xE0;
	
	/**
	 * Special.
	 * {@code iiiixxxx}.
	 */
	public static final int SPECIAL_B =
		0xF0;
	
	/** If equal to constant. */
	public static final int IFEQ_CONST =
		0xE6;
	
	/** Method entry marker. */
	public static final int ENTRY_MARKER =
		0xE7;
	
	/**
	 * New. 
	 * {@code iiiixxxx, r2 = new p1}.
	 */
	public static final int NEW =
		0xF0;
	
	/**
	 * Array length. 
	 * {@code iiiixxxx, r2 = r1.length}.
	 */
	public static final int ARRAYLEN =
		0xF2;
	
	/**
	 * Return. 
	 * {@code iiiixxxx, return}.
	 */
	public static final int RETURN =
		0xF3;
	
	/**
	 * Invoke. 
	 * {@code iiiixxxx}.
	 */
	public static final int INVOKE =
		0xF7;
	
	/** Atomically decrements a memory addres and gets the value. */
	public static final int ATOMIC_INT_DECREMENT_AND_GET =
		0xF9;
	
	/** Atomically increments a memory address. */
	public static final int ATOMIC_INT_INCREMENT =
		0xFA;
	
	/** Load from table that is of native int/pointer size. */
	public static final int LOAD_TABLE =
		0xFC;
	
	/**
	 * Load from pool, note that at code gen time this is aliased.
	 * {@code iiiixxxx}.
	 */
	public static final int LOAD_POOL =
		0xFD;
	
	/**
	 * Compare and exchange. 
	 * {@code iiiixxxx}.
	 */
	public static final int BREAKPOINT =
		0xFF;
}

