// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Swing window interface.
 *
 * @since 2024/03/13
 */
public class SwingWindowInterface
	implements ScritchWindowInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public void callAttention(ScritchWindowBracket __window)
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int contentHeight(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		JFrame frame = ((SwingWindowObject)__window).frame;
		return frame.getContentPane().getHeight();
	}
	
	@Override
	public void contentMinimumSize(@NotNull ScritchWindowBracket __window,
		@Range(from = 1, to = Integer.MAX_VALUE) int __w,
		@Range(from = 1, to = Integer.MAX_VALUE) int __h)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		if (__w <= 0 || __h <= 0)
			throw new MLECallError("Invalid dimensions");
		
		JFrame frame = ((SwingWindowObject)__window).frame;
		frame.getContentPane().setMinimumSize(new Dimension(__w, __h));
		frame.setMinimumSize(new Dimension(__w, __h));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int contentWidth(ScritchWindowBracket __window)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		JFrame frame = ((SwingWindowObject)__window).frame;
		return frame.getContentPane().getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public boolean hasFocus(ScritchWindowBracket __window)
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public boolean isVisible(ScritchWindowBracket __window)
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public int inputTypes(ScritchWindowBracket __window)
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
	
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/13
	 */
	@Override
	public ScritchWindowBracket newWindow()
	{
		return new SwingWindowObject();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void setVisible(@NotNull ScritchWindowBracket __window,
		boolean __visible)
		throws MLECallError
	{
		if (__window == null)
			throw new MLECallError("Null arguments.");
		
		// Center the frame first if it is not visible
		JFrame frame = ((SwingWindowObject)__window).frame;
		if (!frame.isVisible())
			frame.setLocationRelativeTo(null);
		
		// Forward to Swing
		frame.setVisible(__visible);
	}
}
