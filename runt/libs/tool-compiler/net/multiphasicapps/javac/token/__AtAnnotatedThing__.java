// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
* This represents a thing which is annotated.
*
* @since 2018/03/09
*/
final class __AtAnnotatedThing__
	extends __At__
{
	/** The thing being annotated. */
	public final __IsAnnotated__ annotating;
	
	/**
	 * Initializes the state.
	 *
	 * @param __at The thing being annotated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/09
	 */
	public __AtAnnotatedThing__(__IsAnnotated__ __at)
		throws NullPointerException
	{
		super(ContextArea.ANNOTATED_THING);
		
		if (__at == null)
			throw new NullPointerException("NARG");
		
		this.annotating = __at;
	}
}

