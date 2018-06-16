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
 * This is used to represent an annotation which has a plain annotation, this
 * is to make the usage of visible annotations and annotations easier.
 *
 * @since 2018/06/16
 */
public interface IsAnnotation
{
	/**
	 * Returns the representation of the object as a plain annotation.
	 *
	 * @return The plain annotation.
	 * @since 2018/06/16
	 */
	public abstract Annotation annotation();
	
	/**
	 * Returns the value of the given key.
	 *
	 * @param __n The name to obtain.
	 * @return The value for the given element.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/06/16
	 */
	public abstract AnnotationValue get(MethodName __n)
		throws NullPointerException;
	
	/**
	 * Returns the type of this annotation.
	 *
	 * @return The annotation type.
	 * @since 2018/06/16
	 */
	public abstract ClassName type();
}

