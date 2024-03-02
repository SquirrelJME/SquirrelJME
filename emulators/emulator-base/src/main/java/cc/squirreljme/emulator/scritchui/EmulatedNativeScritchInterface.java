// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Emulated version of {@link NativeScritchInterface}.
 *
 * @since 2024/03/01
 */
public class EmulatedNativeScritchInterface
{
	/**
	 * Returns the system native scritch interface.
	 *
	 * @return The native scritch interface.
	 * @throws MLECallError If there is no support for the native interface.
	 * @since 2024/02/29
	 */
	@SquirrelJMEVendorApi
	public static ScritchInterface nativeInterface()
		throws MLECallError
	{
		return new SwingScritchInterface();
	}
}
