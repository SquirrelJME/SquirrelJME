// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.emulator.scritchui.dylib.DylibScritchInterface;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
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
		return DylibScritchInterface.instance();
	}
	
	/**
	 * Same as {@link NativeScritchInterface#panelOnly()}.
	 * 
	 *
	 * @return Same as {@link NativeScritchInterface#panelOnly()}.
	 * @throws MLECallError Same as {@link NativeScritchInterface#panelOnly()}.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	public static boolean panelOnly()
		throws MLECallError
	{
		return false;
	}
}
