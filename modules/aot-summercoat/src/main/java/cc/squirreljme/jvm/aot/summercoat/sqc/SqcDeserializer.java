// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.sqc;

import cc.squirreljme.jvm.aot.summercoat.SummerCoatHandler;
import java.io.InputStream;

/**
 * Deserialize SQCs and allows them to be replayed into later processing
 * stages accordingly.
 *
 * @since 2022/09/02
 */
public final class SqcDeserializer
{
	/** Handler which can accept the deserialized output. */
	protected final SummerCoatHandler handler;
	
	/**
	 * Initializes the deserializer which can be piped into a handler.
	 * 
	 * @param __in The stream to deserialize from.
	 * @param __handler The output handler.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/02
	 */
	public SqcDeserializer(InputStream __in, SummerCoatHandler __handler)
		throws NullPointerException
	{
		if (__in == null || __handler == null)
			throw new NullPointerException("NARG");
			
		this.handler = __handler;
	}
}
