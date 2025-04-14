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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScrollPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for ScritchUI scroll panels.
 *
 * @since 2024/07/29
 */
@SquirrelJMEVendorApi
public interface ScritchScrollPanelInterface
	extends ScritchApiInterface, ScritchViewBracket, ScritchContainerBracket
{
	/**
	 * Creates a new scroll panel.
	 *
	 * @return The newly created scroll panel.
	 * @throws MLECallError If the scroll panel could not be created.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchScrollPanelBracket scrollPanelNew()
		throws MLECallError;
}
