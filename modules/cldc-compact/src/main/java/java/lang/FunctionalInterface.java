// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This is not used by Java ME 8 or SquirrelJME, however it is needed by
 * newer versions of the Java compiler in order to compile modules
 * correctly as it will attempt to annotate interfaces that meet the criteria
 * for functional interfaces automatically.
 *
 * @since 2020/07/14
 */
@Api
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface FunctionalInterface
{
}
