// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * The result of an audio rendering.
 *
 * @since 2025/05/04
 */
@SquirrelJMEVendorApi
public interface AudioRenderResult
{
	/** Continue rendering. */
	@SquirrelJMEVendorApi
	byte CONTINUE =
		0;
	
	/** Stop rendering. */
	@SquirrelJMEVendorApi
	byte STOP =
		1;
}
