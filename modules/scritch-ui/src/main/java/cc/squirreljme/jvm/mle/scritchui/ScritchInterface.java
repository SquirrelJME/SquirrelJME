// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * Main interface for ScritchUI, all the logic calls are made through this
 * initially.
 *
 * @since 2024/02/29
 */
@SquirrelJMEVendorApi
public interface ScritchInterface
{
	/**
	 * Returns the interface which contains information on the environment.
	 *
	 * @return The environment interface.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchEnvironmentInterface environment();
	
	/**
	 * Returns the screen interface.
	 *
	 * @return The screen interface.
	 * @since 2024/03/10
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchScreenInterface screen();
	
	/**
	 * Returns the window interface.
	 *
	 * @return The window interface.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchWindowInterface window();
}
