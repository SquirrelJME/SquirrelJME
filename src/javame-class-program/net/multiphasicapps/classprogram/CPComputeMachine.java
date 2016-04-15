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

import net.multiphasicapps.classfile.CFFieldReference;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This interface is implemented by classes.
 *
 * Destination and source registers are identified by their ID.
 *
 * @param <A> The pass through value.
 * @since 2016/04/08
 */
public interface CPComputeMachine<A>
{
	/**
	 * Allocates an object but does not perform construction of it.
	 *
	 * @param __pa Passed value.
	 * @param __dest Destination register.
	 * @param __cl The class to allocate.
	 * @since 2016/04/09
	 */
	public abstract void allocateObject(A __pa, int __dest,
		ClassNameSymbol __cl);
	
	/**
	 * Copy from one variable to another.
	 *
	 * @param __pa Passed value.
	 * @param __dest The destination variable.
	 * @param __src The source variable.
	 * @since 2016/04/15
	 */
	public abstract void copy(A __pa, int __dest, int __src);
	
	/**
	 * Reads a value from a static field.
	 *
	 * @param __pa Passed value.
	 * @param __dest Destination variable.
	 * @param __f The field to read.
	 * @since 2016/04/15
	 */
	public abstract void getStaticField(A __pa, int __dest,
		CFFieldReference __f);
}

