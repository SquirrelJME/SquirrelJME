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
 * Disable all release optimization, compaction, and shrinking; everything
 * about the class is kept as-is. This should only be used for very-important
 * classes that the virtual machine requires to operate correctly.
 * 
 * This should rarely be used except in cases where ProGuard is having
 * extreme difficulty with keeping a class around.
 *
 * @since 2026/10/01
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@KeepAbsolutelyEverything
public @interface KeepAbsolutelyEverything
{
}

