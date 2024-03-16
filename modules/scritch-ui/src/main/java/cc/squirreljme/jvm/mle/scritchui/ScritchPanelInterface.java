// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for panels.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchPanelInterface
{
	/**
	 * Creates a new panel.
	 *
	 * @return The newly created panel.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchPanelBracket newPanel();
}
