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
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_REG_INT =
		0x00;
	
	/**
	 * Math, R=RR, Long.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_REG_LONG =
		0x10;
	
	/**
	 * Math, R=RR, Float.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_REG_FLOAT =
		0x20;
	
	/**
	 * Math, R=RR, Double.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_REG_DOUBLE =
		0x30;
	
	/**
	 * Int comparison, then maybe jump.
	 * {@code iiiiRccc}.
	 */
	public static final int IF_ICMP =
		0x40;
	
	/**
	 * Memory access, offset is in register.
	 * {@code iiiiLddd}.
	 */
	public static final int MEMORY_OFF_REG =
		0x50;
	
	/**
	 * Atomic memory access, offset is in register.
	 * {@code iiiiLddd}.
	 */
	public static final int ATOMIC_MEMORY_OFF_REG =
		0x60;
	
	/**
	 * Array access.
	 * {@code iiiiLddd}.
	 */
	public static final int ARRAY_ACCESS =
		0x70;
	
	/**
	 * Math, R=RC, Integer.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_CONST_INT =
		0x80;
	
	/**
	 * Math, R=RC, Long.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_CONST_LONG =
		0x90;
	
	/**
	 * Math, R=RC, Float.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_CONST_FLOAT =
		0xA0;
	
	/**
	 * Math, R=RC, Double.
	 * {@code iiiiMMMM}.
	 */
	public static final int MATH_CONST_DOUBLE =
		0xB0;
	
	/**
	 * Conversion.
	 * {@code iiiiFFTT}.
	 */
	public static final int CONVERSION =
		0xC0;
	
	
	/**
	 * Memory access, offset is a constant.
	 * {@code iiiiLddd}.
	 */
	public static final int MEMORY_OFF_ICONST =
		0xD0;
	
	/**
	 * Atomic memory access, offset is a constant.
	 * {@code iiiiLddd}.
	 */
	public static final int ATOMIC_MEMORY_OFF_ICONST =
		0xE0;
	
	/**
	 * Special.
	 * {@code iiiixxxx}.
	 */
	public static final int SPECIAL =
		0xF0;
	
	/** New. */
	public static final int NEW =
		0xF0;
	
	/** New array. */
	public static final int NEWARRAY =
		0xF1;
	
	/** Array length. */
	public static final int ARRAYLEN =
		0xF2;
	
	/** Return. */
	public static final int RETURN =
		0xF3;
	
	/** Lookup Switch. */
	public static final int LOOKUPSWITCH =
		0xF4;
	
	/** Table Switch. */
	public static final int TABLESWITCH =
		0xF5;
	
	/** Monitor enter. */
	public static final int MONITORENTER =
		0xF6;
	
	/** Invoke. */
	public static final int INVOKE =
		0xF7;
	
	/** Reference push. */
	public static final int REF_PUSH =
		0xF8;
	
	/** Reference clear. */
	public static final int REF_CLEAR =
		0xF9;
	
	/** Reference reset. */
	public static final int REF_RESET =
		0xFA;
	
	/** Count. */
	public static final int COUNT =
		0xFB;
	
	/** Uncount. */
	public static final int UNCOUNT =
		0xFC;
	
	/** Breakpoint. */
	public static final int BREAKPOINT =
		0xFD;
	
	/** Monitor exit. */
	public static final int MONITOREXIT =
		0xFE;
	
	/** Load from pool. */
	public static final int LOAD_POOL =
		0xFF;
}

