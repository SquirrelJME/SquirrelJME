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

import net.multiphasicapps.classfile.ByteCode;

/**
 * This class is used to keep track of the exceptions in the methods.
 *
 * @since 2019/03/21
 */
final class __ExceptionTracker__
{
	/**
	 * Initializes the exception tracker.
	 *
	 * @param __bc The source byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/21
	 */
	__ExceptionTracker__(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

