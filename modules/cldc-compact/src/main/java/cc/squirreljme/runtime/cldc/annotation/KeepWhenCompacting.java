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
 * When compacting the SquirrelJME runtime, force this class to be kept.
 *
 * @since 2026/06/03
 */
@Documented
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.METHOD, ElementType.TYPE})
@SquirrelJMEVendorApi
public @interface KeepWhenCompacting
{
}

