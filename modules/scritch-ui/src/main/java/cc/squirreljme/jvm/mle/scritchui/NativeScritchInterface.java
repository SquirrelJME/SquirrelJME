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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is used to obtain the system's native ScritchUI interface, assuming
 * that it has such.
 *
 * @since 2024/02/29
 */
@SquirrelJMEVendorApi
public final class NativeScritchInterface
{
	/**
	 * Not used.
	 *
	 * @since 2024/02/29
	 */
	@SquirrelJMEVendorApi
	private NativeScritchInterface()
	{
	}
	
	/**
	 * Returns the system native scritch interface.
	 *
	 * @return The native scritch interface.
	 * @throws MLECallError If there is no support for the native interface.
	 * @since 2024/02/29
	 */
	@SquirrelJMEVendorApi
	public static native ScritchInterface nativeInterface()
		throws MLECallError;
	
	/**
	 * Checks whether the native interface only supports basic panels.
	 * 
	 * This implies that the following interfaces are supported:
	 * - {@link ScritchComponentInterface}.
	 * - {@link ScritchContainerInterface}.
	 * - {@link ScritchEnvironmentInterface}.
	 * - {@link ScritchEventLoopInterface}.
	 * - {@link ScritchPanelInterface}.
	 * - {@link ScritchWindowInterface}.
	 *
	 * @return If the interface only supports panels.
	 * @throws MLECallError If there is no support for the native interface.
	 * @deprecated Use {@link ScritchEnvironmentInterface#isPanelOnly()}.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	@Deprecated
	public static native boolean panelOnly()
		throws MLECallError;
}
