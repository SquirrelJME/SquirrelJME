// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.annotation;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.Async;

/**
 * This is a tagging annotation which indicates that the given method must be
 * called within the ScritchUI event loop. The event loop typically runs in
 * a single thread as the vast majority of systems, especially retro ones, will
 * break completely when accessed across threads.
 *
 * @since 2024/02/29
 */
@Documented
@Retention(value= RetentionPolicy.CLASS)
@Target(value={ElementType.METHOD})
@SquirrelJMEVendorApi
public @interface ScritchEventLoop
{
}
