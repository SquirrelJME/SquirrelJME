// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is the common interface in which all annotation types are inherited
 * from.
 *
 * If a random class or interface implements this interface and it is not an
 * annotation type, then it is not an annotation.
 *
 * @since 2014/10/13
 */
@Api
public interface Annotation
{
	/**
	 * Returns the annotation type used for this annotation.
	 *
	 * @return The annotation type.
	 * @since 2014/10/13
	 */
	@Api
	Class<? extends Annotation> annotationType();
	
	/**
	 * Checks the logical equality to another annotation type, they are both
	 * considered to be equal when all of their members are equal and they are
	 * of the same type.
	 *
	 * Primitive types are checked directly except for floating point types
	 * which are equality checked through their wrapper classes.
	 *
	 * Floating point NaN values are considered to be equal, that is the
	 * {@code ==} operator is not used.
	 *
	 * {@link String}, {@link Class}, and enumerations are considered equal if
	 * their normal {@code equals()} evaluates to true.
	 *
	 * Two arrays are equal if {@code Arrays.equals(a, b)} evaluates to
	 * {@code true}.
	 *
	 * @param __o The other object to check against.
	 * @return {@code true} if they are logically equivalent.
	 * @since 2014/10/13
	 */
	@Override
	boolean equals(Object __o);
	
	/**
	 * Calculates the hash code which is a sum of the hash codes of its members
	 * in a specifically defined pattern.
	 *
	 * The base hash code for a member is {@code (127 *
	 * ((String)memberName).hashCode()) ^ specialValue}. The variable
	 * {@code specialValue} is defined depending on the
	 * context. If it is a primitive type, then the hash code that would be
	 * returned if they were object types (their wrapper classes) is used. If
	 * an array then {@code Arrays.hashCode()} is used. Otherwise, it is the
	 * normal hashCode of the specified value.
	 *
	 * @return The hash code for this annotation.
	 * @since 2014/10/13
	 */
	@Override
	int hashCode();
	
	/**
	 * Returns an implementation dependent string which represents the
	 * annotation and all of its values.
	 *
	 * @return The string representation of this annotation.
	 * @since 2014/10/13
	 */
	@Override
	String toString();
}

