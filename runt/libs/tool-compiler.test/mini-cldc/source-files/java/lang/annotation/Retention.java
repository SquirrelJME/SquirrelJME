// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

/**
 * This is used to indicate how far into the compilation and runtime process
 * should keep a visible mark on an annotation.
 *
 * @since 2014/10/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Retention
{
	/**
	 * Specifies where the annotation exists during and after compilation of
	 * source code.
	 *
	 * @return The storage of the annotation.
	 * @see RetentionPolicy
	 * @since 2014/10/13
	 */
	RetentionPolicy value();
}

