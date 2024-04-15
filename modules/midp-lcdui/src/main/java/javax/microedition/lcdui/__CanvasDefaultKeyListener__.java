// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Key listener for canvases.
 *
 * @since 2020/10/16
 */
final class __CanvasDefaultKeyListener__
	implements KeyListener
{
	/** The canvas to reference. */
	protected final Reference<Canvas> canvas;
	
	/**
	 * Initializes the key listener.
	 * 
	 * @param __canvas The canvas to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/16
	 */
	__CanvasDefaultKeyListener__(Canvas __canvas)
		throws NullPointerException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		
		this.canvas = new WeakReference<>(__canvas); 
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyPressed(int __kc, int __km)
	{
		Canvas widget = this.canvas.get();
		if (widget != null)
			widget.keyPressed(__kc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyReleased(int __kc, int __km)
	{
		Canvas widget = this.canvas.get();
		if (widget != null)
			widget.keyReleased(__kc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyRepeated(int __kc, int __km)
	{
		Canvas widget = this.canvas.get();
		if (widget != null)
			widget.keyRepeated(__kc);
	}
}
