// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Mutable i-mode image.
 *
 * @since 2024/01/06
 */
class __MutableImage__
	extends Image
{
	/**
	 * Initializes the base mutable image.
	 *
	 * @param __midpImage The MIDP image to use.
	 * @param __bgColor The background color used for the image.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	__MutableImage__(javax.microedition.lcdui.Image __midpImage,
		__BGColor__ __bgColor)
		throws NullPointerException
	{
		super(__midpImage, __bgColor);
	}
	
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
}
