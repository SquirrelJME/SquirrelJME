// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;

/**
 * This is the event callback which is called whenever the display system
 * needs an event to happen.
 *
 * @since 2018/12/03
 */
final class __EventCallback__
	implements NativeDisplayEventCallback
{
	/** The callback for events. */
	static final __EventCallback__ _CALLBACK =
		new __EventCallback__();
	
	/** Is this registered? */
	volatile boolean _registered;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void command(int __d, int __c)
	{
		try
		{
			Display.__mapDisplay(__d).__doCommandAction(__c);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void exitRequest(int __d)
	{
		try
		{
			Display.__mapDisplay(__d).__doExitRequest();
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void keyEvent(int __d, int __ty, int __kc, int __ch,
		int __time)
	{
		try
		{
			Display.__mapDisplay(__d).__doKeyAction(__ty, __kc,
				(__ch < 0 ? 0 : (char)__ch), __time);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public final void lostCallback()
	{
		todo.DEBUG.note("Lost callback.");
		synchronized (this)
		{
			this._registered = false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void paintDisplay(int __d, int __x, int __y,
		int __w, int __h)
	{
		try
		{
			Display.__mapDisplay(__d).__doRepaint(__x, __y, __w, __h);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void pointerEvent(int __d, int __ty, int __x, int __y,
		int __time)
	{
		try
		{
			Display.__mapDisplay(__d).
				__doPointerAction(__ty, __x, __y, __time);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void shown(int __d, int __shown)
	{
		try
		{
			Display.__mapDisplay(__d).__doDisplayShown(__shown != 0);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void sizeChanged(int __d, int __w, int __h)
	{
		try
		{
			Display.__mapDisplay(__d).__doDisplaySizeChanged(__w, __h);
		}
		catch (Exception t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * Registers the display.
	 *
	 * @since 2018/12/10
	 */
	final void __register()
	{
		// Do not allow the variable to go crazy
		synchronized (this)
		{
			// Only register once!
			if (!this._registered)
			{
				NativeDisplayAccess.registerEventCallback(_CALLBACK);
				this._registered = true;
			}
		}
	}
}

