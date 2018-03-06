// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents any element which has been annotated.
 *
 * @since 2018/03/06
 */
public interface Annotated
{
	/**
	 * Returns all of the annotations which have been specified.
	 *
	 * @return The annotated values.
	 * @since 2018/03/06
	 */
	public abstract AnnotatedValue[] annotatedValues();
}

