// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * This is used to obtain the system's native ScritchUI interface, assuming
 * that it has such.
 *
 * @since 2024/02/29
 */
public final class NativeScritchUIShelf
{
	/**
	 * Not used.
	 *
	 * @since 2024/02/29
	 */
	private NativeScritchUIShelf()
	{
	}
	
	/**
	 * Returns the system native scritch interface.
	 *
	 * @return The native scritch interface.
	 * @throws MLECallError If there is no support for the native interface.
	 * @since 2024/02/29
	 */
	public static native ScritchInterface nativeInterface()
		throws MLECallError;
}
