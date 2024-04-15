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
 * Specifies the context in which the annotation is valid.
 *
 * @since 2014/10/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Api
public @interface Target
{
	/**
	 * A list of valid contexts where the annotation may be used.
	 *
	 * @return An array of permitted targets.
	 * @since 2014/10/13
	 */
	@Api
	ElementType[] value();
}

