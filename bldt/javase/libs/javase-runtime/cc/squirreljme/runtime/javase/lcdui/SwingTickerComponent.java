// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.ui.UiTicker;
import cc.squirreljme.runtime.lcdui.ui.UiTickerListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;

/**
 * This is the compoent which is shown to display text.
 *
 * @since 2018/03/27
 */
public class SwingTickerComponent
	extends JScrollPane
	implements ActionListener, ComponentListener, UiTickerListener
{
	/** The label. */
	protected final JLabel label;
	
	/** Timer used to scroll things. */
	protected final Timer timer;
	
	/** Moving to the right? */
	private boolean _right =
		true;
	
	/**
	 * Initializes the component.
	 *
	 * @since 2018/03/27
	 */
	public SwingTickerComponent()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//AS_NEEDED);
		
		JLabel label = new JLabel();
		this.label = label;
		
		// Only the label is here
		this.setViewportView(label);
		this.addComponentListener(this);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		
		// Setup base timer for scrolling
		Timer timer = new Timer(200, this);
		this.timer = timer;
		
		// Make it so the user cannot scroll the message
		this.setWheelScrollingEnabled(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void actionPerformed(ActionEvent __e)
	{
		JViewport vp = this.getViewport();
		JLabel label = this.label;
		
		// Moving to the right?
		boolean right = this._right;
		
		// Determine where we are and the end position
		Point pos = vp.getViewPosition();
		int vsx = pos.x,
			vsw = vp.getSize().width,
			lbw = label.getWidth(),
			ren = vsx + vsw;
		
		// Calculate new position
		int nxx = vsx + (right ? 10 : -10);
		
		// Change position?
		if (nxx < 0)
			this._right = true;
		else if (ren >= lbw)
			this._right = false;
		
		// Set new position
		vp.setViewPosition(new Point(nxx, pos.y));
		//vp.revalidate();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void changedTickerString(UiTicker __lcd, String __text)
		throws NullPointerException
	{
		this.label.setText(__text);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void componentHidden(ComponentEvent __e)
	{
		Timer timer = this.timer;
		
		// Do not do scrolling if it is not even seen
		if (timer.isRunning())
			this.timer.stop();
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void componentMoved(ComponentEvent __e)
	{
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void componentResized(ComponentEvent __e)
	{
		Timer timer = this.timer;
		boolean running = timer.isRunning();
		
		if (this.__runTimer())
		{
			if (!running)
				this.timer.start();
		}
		else
		{
			if (running)
				this.timer.stop();
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/27
	 */
	@Override
	public void componentShown(ComponentEvent __e)
	{
		Timer timer = this.timer;
		
		if (this.__runTimer())
			if (!timer.isRunning())
				this.timer.start();
	}
	
	/**
	 * Checks whether the timer should be ran.
	 *
	 * @return If the timer should be ran.
	 * @since 2018/03/27
	 */
	private final boolean __runTimer()
	{
		JLabel label = this.label;
		
		// Do not run when it is invisible
		if (!this.isVisible() || !label.isVisible())
			return false;
		
		return this.getViewport().getSize().width < label.getWidth();
	}
}

