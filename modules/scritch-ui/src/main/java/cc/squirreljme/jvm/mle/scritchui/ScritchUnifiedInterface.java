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

/**
 * Unified interface which has all ScritchUI interfaces.
 *
 * @since 2024/08/02
 */
@SuppressWarnings("OverlyCoupledClass")
@SquirrelJMEVendorApi
public interface ScritchUnifiedInterface
	extends ScritchChoiceInterface,
		ScritchComponentInterface,
		ScritchContainerInterface,
		ScritchEnvironmentInterface,
		ScritchEventLoopInterface,
		ScritchInterface,
		ScritchLabelInterface,
		ScritchLAFInterface,
		ScritchListInterface,
		ScritchMenuInterface,
		ScritchPaintableInterface,
		ScritchPanelInterface,
		ScritchScreenInterface,
		ScritchScrollPanelInterface,
		ScritchViewInterface,
		ScritchWindowInterface
{
}
