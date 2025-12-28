// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;

/**
 * Binder between the browser and media player.
 *
 * @since 2025/12/27
 */
public final class Binder
{
	/** Reference to self. */
	private final Reference<Binder> _self =
		new WeakReference<>(this);
	
	/** The browser. */
	final BasicBrowser _browser =
		new BasicBrowser(this._self);
	
	/** The media player. */
	final MediaPlayer _player =
		new MediaPlayer(this._self);
	
	/** The display to use. */
	final Display _display;
	
	/**
	 * Initializes the binder.
	 *
	 * @param __display The display to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	public Binder(Display __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this._display = __display;
	}
}
