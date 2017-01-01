// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

/**
 * Specifies the context in which the annotation is valid.
 *
 * @since 2014/10/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Target
{
	/**
	 * A list of valid contexts where the annotation may be used.
	 *
	 * @return An array of permitted targets.
	 * @since 2014/10/13
	 */
	ElementType[] value();
}

