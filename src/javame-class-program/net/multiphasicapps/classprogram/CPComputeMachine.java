// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This interface is implemented by classes.
 *
 * Destination and source registers are identified by their ID.
 *
 * @param <A> The first pass through value.
 * @param <B> The second pass through value.
 * @since 2016/04/08
 */
public interface CPComputeMachine<A, B>
{
	/**
	 * Allocates an object but does not perform construction of it.
	 *
	 * @param __pa Passed A.
	 * @param __pb Passed B.
	 * @param __dest Destination register.
	 * @param __cl The class to allocate.
	 * @since 2016/04/09
	 */
	public abstract void allocateObject(A __pa, B __pb, int __dest,
		ClassNameSymbol __cl);
}

