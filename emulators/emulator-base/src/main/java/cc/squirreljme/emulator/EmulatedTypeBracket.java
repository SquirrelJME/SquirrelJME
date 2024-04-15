// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Represents an emulated type bracket.
 *
 * @since 2022/09/05
 */
public class EmulatedTypeBracket
	implements TypeBracket
{
	/** The Java class this represents. */
	public final Class<?> javaClass;
	
	/**
	 * Initializes the type bracket.
	 * 
	 * @param __javaClass The Java class used.
	 * @throws MLECallError On null arguments.
	 * @since 2022/09/05
	 */
	public EmulatedTypeBracket(Class<?> __javaClass)
		throws MLECallError
	{
		if (__javaClass == null)
			throw new MLECallError("NARG");
		
		this.javaClass = __javaClass;
	}
}
