// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * Represents the type of operation to perform.
 *
 * @since 2019/03/17
 */
public interface RegisterOperationType
{
	/** No operation. */
	public static final int NOP =
		0;
	
	/** Copy narrow. */
	public static final int NARROW_COPY =
		21;
	
	/** Copy narrow and reference count destination. */
	public static final int NARROW_COPY_AND_COUNT_DEST =
		22;
	
	/** Copy wide. */
	public static final int WIDE_COPY =
		23;
	
	/** Narrow constant. */
	public static final int NARROW_CONST =
		24;
	
	/** Wide constant. */
	public static final int WIDE_CONST =
		25;
	
	/** Invoke pointer from constant pool. */
	public static final int INVOKE_FROM_POOL =
		26;
	
	/** Invoke pointer. */
	public static final int INVOKE_POINTER =
		27;
	
	/** Reference count. */
	public static final int COUNT =
		28;
	
	/** Un-reference count. */
	public static final int UNCOUNT =
		29;
	
	/** Jump if exception flag is set. */
	public static final int JUMP_ON_EXCEPTION =
		210;
	
	/** Return from method. */
	public static final int RETURN =
		211;
	
	/** Jump if class matches, load/clear exception to specified register. */
	public static final int EXCEPTION_CLASS_JUMP =
		212;
	
	/** Jump to address. */
	public static final int JUMP =
		213;
	
	/** Allocate class. */
	public static final int ALLOCATE_CLASS =
		214;
	
	/** Write to a memory address using an offset from a pool. */
	public static final int WRITE_POINTER_WITH_POOL_OFFSET =
		215;
	
	/** Allocate array. */
	public static final int ALLOCATE_ARRAY =
		216;
	
	/** Compare int against zero and jump. */
	public static final int IF_INT_COMP_ZERO_THEN_JUMP =
		217;
	
	/** Narrow constant from constant pool. */
	public static final int NARROW_CONST_FROM_POOL =
		218;
	
	/** Set exception flag and store exception. */
	public static final int SET_EXCEPTION =
		219;
	
	/** Read from a memory address using an offset from a pool. */
	public static final int READ_POINTER_WITH_POOL_OFFSET =
		220;
}

