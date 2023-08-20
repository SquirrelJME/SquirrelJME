// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.java.shelf;

import cc.squirreljme.jvm.mle.annotation.SquirrelJMENativeShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Native shelf interface for Java File access.
 *
 * @since 2023/08/20
 */
@SquirrelJMEVendorApi
@SquirrelJMENativeShelf
public final class JavaNioShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2023/08/20
	 */
	private UnixNioShelf()
	{
	}
}
