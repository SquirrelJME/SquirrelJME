// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Displayable;

/**
 * Base DoJa Frame.
 *
 * @since 2025/06/01
 */
@SquirrelJMEVendorApi
public abstract class DoJaFrame
{
	/**
	 * Returns the {@link Displayable} this wraps.
	 *
	 * @return The MIDP {@link Displayable} used.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	protected abstract Displayable __squirreljmeDisplayable();
}
