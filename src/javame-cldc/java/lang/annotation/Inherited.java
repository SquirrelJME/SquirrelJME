// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang.annotation;

/**
 * Indicates that the annotation type should automatically be inherited so that
 * the subclass type appears to have this annotation when it is requested. That
 * is, if an annotation with this annotation is requested it will keep going up
 * the superclasses until it is found.
 *
 * This only affects classes which use an annotation with this annotation, as
 * such interfaces are excluded.
 *
 * @since k8 2014/10/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Inherited
{
}

