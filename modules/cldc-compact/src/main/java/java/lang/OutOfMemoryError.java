// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when the virtual machine is out of memory.
 *
 * @since 2018/12/04
 */
@Api
public class OutOfMemoryError
	extends VirtualMachineError
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	@Api
	public OutOfMemoryError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	@Api
	public OutOfMemoryError(String __m)
	{
		super(__m);
	}
}
