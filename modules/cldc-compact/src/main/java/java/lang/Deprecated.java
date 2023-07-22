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
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a flagging annotation which may be attached to classes, fields,
 * and methods to indicate that it should no longer be used in the future and
 * alternatives should be searched for and used instead.
 *
 * @since 2016/04/12
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE,
	ElementType.PARAMETER, ElementType.TYPE})
@Api
public @interface Deprecated
{
}
