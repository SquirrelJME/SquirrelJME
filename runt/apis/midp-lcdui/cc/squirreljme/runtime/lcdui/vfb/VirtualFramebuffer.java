// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.vfb;

import cc.squirreljme.jvm.IPCCallback;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is a virtual framebuffer which may be used by non-SquirrelJME JVMs and
 * by higher level JVMs to allow for IPC based graphics to be used.
 *
 * @since 2019/12/28
 */
public final class VirtualFramebuffer
	extends Canvas
{
	/** The callback to invoke with screen actions. */
	protected final IPCCallback ipc;
	
	/**
	 * Initializes the virtual framebuffer.
	 *
	 * @param __ipc The callback to forward events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public VirtualFramebuffer(IPCCallback __ipc)
		throws NullPointerException
	{
		if (__ipc == null)
			throw new NullPointerException("NARG");
		
		this.ipc = __ipc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void keyPressed(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void keyReleased(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void keyRepeated(int __kc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void paint(Graphics __g)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void pointerDragged(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void pointerPressed(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void pointerReleased(int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void showNotify()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/28
	 */
	@Override
	public final void sizeChanged(int __w, int __h)
	{
		throw new todo.TODO();
	}
}

