// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.pipe;

import cc.squirreljme.jvm.aot.java.ClassHandler;
import cc.squirreljme.jvm.aot.summercoat.SummerCoatHandler;

/**
 * Takes any input byte code that is being processed and outputs it to
 * SummerCoat.
 *
 * @since 2022/09/07
 */
public class JavaToSummerCoatHandler
	implements ClassHandler
{
	/** The handler for SummerCoat output. */
	protected final SummerCoatHandler handler;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __handler The handler for SummerCoat output.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	public JavaToSummerCoatHandler(SummerCoatHandler __handler)
		throws NullPointerException
	{
		if (__handler == null)
			throw new NullPointerException("NARG");
		
		this.handler = __handler;
	}
}
