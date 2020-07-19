// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.vfb;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is a virtualized framebuffer that provides a LCDUI canvas.
 *
 * @see VirtualFramebuffer
 * @since 2019/12/28
 */
@Deprecated
public final class VirtualFramebufferCanvas
	extends Canvas
{
	/** The framebuffer to use. */
	@Deprecated
	protected final VirtualFramebuffer framebuffer;
	
	/**
	 * Initializes the framebuffer canvas.
	 *
	 * @param __fb The framebuffer.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	@Deprecated
	public VirtualFramebufferCanvas(VirtualFramebuffer __fb)
		throws NullPointerException
	{
		if (__fb == null)
			throw new NullPointerException("NARG");
		
		this.framebuffer = __fb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void keyPressed(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void keyReleased(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void keyRepeated(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void paint(Graphics __g)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void pointerDragged(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void pointerPressed(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void pointerReleased(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void showNotify()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Deprecated
	@Override
	public final void sizeChanged(int __w, int __h)
	{
		throw new todo.TODO();
	}
}

