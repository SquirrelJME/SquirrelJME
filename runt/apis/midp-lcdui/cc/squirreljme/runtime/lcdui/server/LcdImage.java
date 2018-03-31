// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents an image which requires a native representation on the
 * server for it to be used properly.
 *
 * @since 2018/03/30
 */
public abstract class LcdImage
	extends LcdCollectable
{
	/**
	 * Initializes the image.
	 *
	 * @param __h The image handle.
	 * @since 2018/03/30
	 */
	public LcdImage(int __h)
	{
		super(__h, CollectableType.IMAGE);
	}
}

