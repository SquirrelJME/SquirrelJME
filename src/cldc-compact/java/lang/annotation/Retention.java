// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

