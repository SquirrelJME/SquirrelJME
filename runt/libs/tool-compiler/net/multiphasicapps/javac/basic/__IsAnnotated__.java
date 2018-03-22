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

import java.util.Collection;

/**
 * This is associated with anything that can be annotated.
 *
 * @since 2018/03/21
 */
@Deprecated
interface __IsAnnotated__
{
	/**
	 * Returns the collection where parsed annotations will go.
	 *
	 * @return The destination collection to place parsed annotation in.
	 * @since 2018/03/21
	 */
	public abstract Collection<AttachedAnnotation> getAnnotations();
}

