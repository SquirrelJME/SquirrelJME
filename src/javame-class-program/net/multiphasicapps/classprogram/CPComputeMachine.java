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
import net.multiphasicapps.classfile.CFMethodReference;
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
	
	/**
	 * Invokes the given method.
	 *
	 * @param __pa Passed value.
	 * @param __dest The destination local variable, will be negative if there
	 * is no return value.
	 * @param __ref The reference to the method.
	 * @param __type The type of invocation to perform.
	 * @param __args The variables which act as arguments of the method.
	 * @since 2016/04/15
	 */
	public abstract void invoke(A __pa, int __dest, CFMethodReference __ref,
		CPInvokeType __type, int... __args);
	
	/**
	 * Places a value into the specified static field.
	 *
	 * @param __pa Passed value.
	 * @param __f The destination static variable.
	 * @param __src The source variable which contains the value.
	 * @since 2016/04/16
	 */
	public abstract void putStaticField(A __pa, CFFieldReference __f,
		int __src);
	
	/**
	 * Returns from the given method returning the specified value.
	 *
	 * @param __pa Passed value.
	 * @param __src The value to be returned, if negative then not value is
	 * returned.
	 * @since 2016/04/16
	 */
	public abstract void returnValue(A __pa, int __src);
	
	/**
	 * Sets the given value to the given string constant.
	 *
	 * @param __pa Passed value.
	 * @param __dest Destination variable.
	 * @param __c The string constant to set.
	 * @since 2016/04/19
	 */
	public abstract void setConstant(A __pa, int __dest, String __c);
	
	/**
	 * Tosses an exception so that it may potentially be handled by exception
	 * handlers.
	 *
	 * @param __pa Passed value.
	 * @param __object The object to throw.
	 * @since 2016/04/16 
	 */
	public abstract void tossException(A __pa, int __object);
}

