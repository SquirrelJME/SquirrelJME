// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Bootstrap main for the JVM library.
 *
 * @since 2025/07/14
 */
@SquirrelJMEVendorApi
public final class LibJvmBootstrap
{
	/**
	 * Main entry point for the bootstrap library.
	 *
	 * @param __ignored The arguments passed to the bootstrap, these options
	 * are not considered nor processed.
	 * @throws Throwable Any thrown throwable.
	 * @since 2025/07/14
	 */
	@SquirrelJMEVendorApi
	public static native void main(String... __ignored)
		throws Throwable;
}
