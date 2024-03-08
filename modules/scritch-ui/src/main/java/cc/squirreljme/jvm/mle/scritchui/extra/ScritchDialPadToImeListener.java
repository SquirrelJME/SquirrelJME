// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.extra;

import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputDialPadListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputNumPadListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Base interface for mapped IMEs.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchDialPadToImeListener
	extends ScritchInputDialPadListener, ScritchInputNumPadListener
{
}
