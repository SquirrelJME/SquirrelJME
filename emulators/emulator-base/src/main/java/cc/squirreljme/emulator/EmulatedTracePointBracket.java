// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.TracePointBracket;

/**
 * Emulates {@link TracePointBracket}.
 *
 * @since 2023/07/19
 */
public class EmulatedTracePointBracket
	implements TracePointBracket
{
	/** The trace element. */
	protected final StackTraceElement element;
	
	/**
	 * Initializes the emulated trace point.
	 *
	 * @param __element The original stack trace element.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/19
	 */
	public EmulatedTracePointBracket(StackTraceElement __element)
		throws NullPointerException
	{
		if (__element == null)
			throw new NullPointerException("NARG");
		
		this.element = __element;
	}
}
