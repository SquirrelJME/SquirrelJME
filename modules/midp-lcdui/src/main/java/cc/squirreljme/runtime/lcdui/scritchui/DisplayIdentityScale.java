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
 * No screen scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public class DisplayIdentityScale
	implements DisplayScale
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenY(int __y)
	{
		return __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureY(int __y)
	{
		return __y;
	}
}
