// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a flagging annotation which is attached to a method to indicate that
 * it overrides a method in the super class. When the compiler encounters
 * this, it makes sure that it actually overrides a method. If it does not
 * then a compilation error occurs. Using this all the time is recommended when
 * a method is intended to be overidden.
 *
 * As an example, if the base class has the following method:
 *
 * {@code
 * public static int foo(long __a);
 * }
 *
 * And a class which extends the base class has the following method:
 *
 * {@code
 * public static int foo(int __a);
 * }
 *
 * If it is intended to add a new {@code foo} which handles {@code int} instead
 * of {@code long} then this annotation should not be used, however it it is
 * intended to replace it (and specifying {@code int} was a mistake) then this
 * should be used.
 *
 * @since 2016/04/12
 */
@Target(value={ElementType.METHOD})
@Retention(value=RetentionPolicy.SOURCE)
@Api
public @interface Override
{
}


