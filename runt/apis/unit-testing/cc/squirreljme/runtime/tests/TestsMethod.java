// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.tests;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is attached to a test method and indicates that it tests the specified
 * method in a given class.
 *
 * @since 2018/03/06
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TestsMethod
{
	/**
	 * The class which is being tested.
	 *
	 * @since 2018/03/06
	 */
	Class<?> testedClass();
	
	/**
	 * This specifies the method being tested.
	 *
	 * @since 2018/03/06
	 */
	String testedMethodName();
	
	/**
	 * This specifies the descriptor of the method being tested, this uses
	 * the standard JVM syntax for methods.
	 *
	 * @since 2018/03/06
	 */
	String testedDescriptor();
}

