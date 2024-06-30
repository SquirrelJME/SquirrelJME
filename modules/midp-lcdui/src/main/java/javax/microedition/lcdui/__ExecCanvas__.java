// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Not Described.
 *
 * @since 2024/06/30
 */
abstract class __ExecCanvas__
{
	/** The canvas to inform. */
	final Reference<Canvas> _canvas;
	
	/**
	 * Initializes the callback.
	 *
	 * @param __canvas The canvas to call for.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/30
	 */
	__ExecCanvas__(Canvas __canvas)
		throws NullPointerException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		
		this._canvas = new WeakReference<>(__canvas);
	}
}
