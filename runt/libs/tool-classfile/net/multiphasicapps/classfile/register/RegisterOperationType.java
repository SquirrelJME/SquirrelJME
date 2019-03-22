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
		1;
	
	/** Copy narrow and reference count destination. */
	public static final int NARROW_COPY_AND_COUNT_DEST =
		2;
	
	/** Copy wide. */
	public static final int WIDE_COPY =
		3;
	
	/** Narrow constant. */
	public static final int NARROW_CONST =
		4;
	
	/** Wide constant. */
	public static final int WIDE_CONST =
		5;
	
	/** Invoke pointer from constant pool. */
	public static final int INVOKE_FROM_CONSTANT_POOL =
		6;
	
	/** Invoke pointer. */
	public static final int INVOKE_POINTER =
		7;
	
	/** Reference count. */
	public static final int COUNT =
		8;
	
	/** Un-reference count. */
	public static final int UNCOUNT =
		9;
	
	/** Jump if exception flag is set, clear exception flag. */
	public static final int JUMP_ON_EXCEPTION_AND_CLEAR =
		10;
	
	/** Return from method. */
	public static final int RETURN =
		11;
}

