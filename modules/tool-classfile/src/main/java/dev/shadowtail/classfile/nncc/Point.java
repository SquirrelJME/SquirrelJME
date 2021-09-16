// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

/**
 * This stores the information for a single point in the native code.
 *
 * @since 2019/04/26
 */
public final class Point
{
	/** The instruction used. */
	public final NativeInstruction instruction;
	
	/**
	 * Initializes the instruction point.
	 *
	 * @param __i The instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/26
	 */
	public Point(NativeInstruction __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.instruction = __i;
	}
}
