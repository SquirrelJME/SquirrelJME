// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.ApiStatus;

/**
 * Provider for pencil fonts.
 *
 * @since 2024/06/25
 */
@SquirrelJMEVendorApi
public abstract class PencilFontProvider
{
	/**
	 * Returns the used internal pencil font.
	 *
	 * @return The used internal pencil font.
	 * @since 2024/06/25
	 */
	@SquirrelJMEVendorApi
	@ApiStatus.Internal
	protected abstract PencilFontBracket __squirreljmePencilFont();
}
