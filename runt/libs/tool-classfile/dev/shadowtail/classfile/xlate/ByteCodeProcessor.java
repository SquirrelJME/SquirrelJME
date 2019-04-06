// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.ByteCode;

/**
 * This class goes through the byte code for a method and then performs stack
 * and instruction work on it.
 *
 * @since 2019/04/06
 */
public final class ByteCodeProcessor
{
	/** The input byte code to be read. */
	protected final ByteCode bytecode;
	
	/**
	 * Initializes the byte code processor.
	 *
	 * @param __bc The target byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public ByteCodeProcessor(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
	}
}

