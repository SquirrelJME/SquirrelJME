// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;

/**
 * This is thrown when the stack overflows, note that CLDC does not define
 * this error.
 *
 * @deprecated CLDC does not have this error.
 * @since 2023/07/09
 */
@Deprecated
@ApiDefinedDeprecated("CLDC does not have this error.")
public class StackOverflowError
	extends VirtualMachineError
{
	/**
	 * Initializes the exception.
	 * 
	 * @since 2023/07/09
	 */
	@Deprecated
	@ApiDefinedDeprecated("CLDC does not have this error.")
	public StackOverflowError()
	{
		super();
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message to the exception.
	 * @since 2023/07/09
	 */
	@Deprecated
	@ApiDefinedDeprecated("CLDC does not have this error.")
	public StackOverflowError(String __m)
	{
		super(__m);
	}
}
