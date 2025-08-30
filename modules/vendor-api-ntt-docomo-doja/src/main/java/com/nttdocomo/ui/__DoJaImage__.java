// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

/**
 * Simple wrapper as Image is abstract.
 *
 * @since 2025/05/05
 */
final class __DoJaImage__
	extends Image
{
	/**
	 * Initializes the base image provider.
	 *
	 * @param __image The image being wrapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	__DoJaImage__(javax.microedition.lcdui.Image __image)
		throws NullPointerException
	{
		super(__image, null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void dispose()
	{
		// Does nothing
	}
}
