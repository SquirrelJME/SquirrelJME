// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;

/**
 * This represents a native canvas which is required by all implementations to
 * be supported by the native window system.
 *
 * @since 2017/05/24
 */
public abstract class NativeCanvas
	extends NativeDisplayable
{
	/**
	 * Initializes the native canvas.
	 *
	 * @param __ref The reference to the LCDUI canvas.
	 * @since 2017/05/24
	 */
	public NativeCanvas(Reference<Displayable> __ref)
	{
		super(__ref);
	}
}

