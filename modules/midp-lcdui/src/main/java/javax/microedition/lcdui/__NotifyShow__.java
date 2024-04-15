// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * Notifies that the displayable has been shown.
 *
 * @since 2021/11/28
 */
final class __NotifyShow__
	implements Runnable
{
	private final Displayable _show;
	
	/**
	 * Initializes the notifier.
	 * 
	 * @param __show The displayable being shown.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	__NotifyShow__(Displayable __show)
		throws NullPointerException
	{
		if (__show == null)
			throw new NullPointerException("NARG");
		
		this._show = __show;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public void run()
	{
		Displayable show = this._show;
		show.__showNotify(show);
	}
}
