// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Floating point scaling for displays.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public class DisplayFloatScale
	implements DisplayScale
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenX(int __x)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenY(int __y)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureX(int __x)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureY(int __y)
	{
		throw Debugging.todo();
	}
}
