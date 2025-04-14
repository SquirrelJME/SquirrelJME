// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * SquirrelJME specific reflection oriented methods.
 *
 * @since 2022/09/07
 */
@SquirrelJMEVendorApi
public final class ReflectionShelf
{
	/**
	 * Not used
	 * 
	 * @since 2022/09/07
	 */
	@SquirrelJMEVendorApi
	private ReflectionShelf()
	{
	}
	
	/**
	 * Invokes the main method of a give type.
	 * 
	 * @param __type The type to look for the main method in.
	 * @param __args The arguments to the call.
	 * @throws MLECallError If any argument is {@code null}.
	 * @throws Throwable Any exception thrown by the target.
	 * @since 2022/09/07
	 */
	@SquirrelJMEVendorApi
	public static native void invokeMain(@NotNull TypeBracket __type,
		@NotNull String... __args)
		throws MLECallError, Throwable;
}
