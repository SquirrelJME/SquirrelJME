// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify that the given method is serialized and
 * will never be called in parallel ever by the LCDUI display driver, it does
 * not have any effect on the method and is just a documentation indicator.
 *
 * @since 2017/08/19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@SquirrelJMEVendorApi
public @interface SerializedEvent
{
}

