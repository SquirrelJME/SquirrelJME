// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This is a backend which utilizes the SquirrelJME UI interface for all of
 * the related drawing applications.
 *
 * @since 2019/05/16
 */
public final class NativeUIBackend
	implements PhoneDisplayBackend
{
	/** The native display ID. */
	protected final int nid;
	
	/**
	 * Initializes the native UI backend using the given display ID.
	 *
	 * @param __nid The native display ID this uses.
	 * @since 2019/05/16
	 */
	public NativeUIBackend(int __nid)
	{
		this.nid = __nid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final boolean isUpsidedown()
	{
		throw new todo.TODO();
		/*
		NativeDisplayAccess.isUpsideDown(this._nid)
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/16
	 */
	@Override
	public final PixelFormat pixelFormat()
	{
		throw new todo.TODO();
		/*
		this._state.framebuffer().pixelformat
		*/
	}
	
	/*
	NativeDisplayAccess.displayRepaint(state.nativeid, 0, 0, w, h);
	*/
}

