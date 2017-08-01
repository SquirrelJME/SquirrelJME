// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.lcdui.DisplayState;
import net.multiphasicapps.squirreljme.lcdui.NativeCanvas;
import net.multiphasicapps.squirreljme.lcdui.NativeDisplay;
import net.multiphasicapps.squirreljme.lcdui.NativeFont;

/**
 * This is a display which implements the native display interface on top of
 * Java's Swing interface.
 *
 * @since 2017/05/23
 */
public class SwingNativeDisplay
	extends NativeDisplay
{
	/** The single Swing display head instance, only one is needed. */
	protected final SwingHead head =
		new SwingHead();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public NativeCanvas createCanvas(Reference<Displayable> __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new SwingCanvas(__ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/25
	 */
	@Override
	public int fontPixelSize(int __sz)
	{
		switch (__sz)
		{
			case javax.microedition.lcdui.Font.SIZE_LARGE:
				throw new todo.TODO();
				
			case javax.microedition.lcdui.Font.SIZE_SMALL:
				throw new todo.TODO();
				
			default:
				throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/25
	 */
	@Override
	public NativeFont[] fonts()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public NativeDisplay.Head[] heads()
	{
		return new NativeDisplay.Head[]{this.head};
	}
	
	/**
	 * This is class used for Swing display heads.
	 *
	 * @since 2017/05/24
	 */
	public class SwingHead
		extends NativeDisplay.Head
	{
		/**
		 * Initializes the swing head.
		 *
		 * @since 2017/05/24
		 */
		protected SwingHead()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/24
		 */
		@Override
		public int getContentHeight()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/24
		 */
		@Override
		public int getMaximumHeight()
		{
			return GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDisplayMode().getHeight();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/24
		 */
		@Override
		public int getMaximumWidth()
		{
			return GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDisplayMode().getWidth();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/24
		 */
		@Override
		public int getContentWidth()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/24
		 */
		public void setState(DisplayState __s)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// This has no effect on swing because there is no foreground
			// or background states
		}
	}
}

