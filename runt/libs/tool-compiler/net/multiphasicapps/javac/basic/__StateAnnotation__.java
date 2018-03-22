// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

/**
 * This stores the state for the annotation parser which is attached to
 * something that is annotated.
 *
 * @since 2018/03/21
 */
@Deprecated
final class __StateAnnotation__
	extends __State__
{
	/** The thing being annotated. */
	protected final __IsAnnotated__ what;
	
	/**
	 * Initializes the state.
	 *
	 * @param __ia The thing that is annotated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	__StateAnnotation__(__IsAnnotated__ __ia)
		throws NullPointerException
	{
		super(__State__.Area.ANNOTATION);
		
		if (__ia == null)
			throw new NullPointerException("NARG");
		
		this.what = __ia;
	}
}

