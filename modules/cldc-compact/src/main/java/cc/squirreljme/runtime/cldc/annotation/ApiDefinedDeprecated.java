// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The API defines this as being deprecated however we do not use
 * {@link Deprecated} as that would emit warnings and interfere with
 * SquirrelJME needing to use such annotation.
 *
 * @since 2020/10/03
 */
@Documented
@Retention(value= RetentionPolicy.SOURCE)
@Target(value={ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE,
	ElementType.PARAMETER, ElementType.TYPE})
public @interface ApiDefinedDeprecated
{
	/** The reason this is deprecated. */
	String value() default "";
}
