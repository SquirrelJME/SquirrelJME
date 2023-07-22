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
 * This represents an exported SquirrelJME API, one which is intended to be
 * used by third parties and otherwise on specifically SquirrelJME.
 *
 * @since 2023/01/27
 */
@Documented
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE,
	ElementType.PARAMETER, ElementType.TYPE})
@SquirrelJMEVendorApi
public @interface SquirrelJMEVendorApi
{
	/** @return The API version. */
	String value() default "";
}

