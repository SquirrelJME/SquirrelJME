// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.emulator.NativeGameController;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UISpecialCode;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Standard canvas item.
 *
 * @since 2020/07/18
 */
public class SwingItemCanvas
	extends SwingItem
{
	/** The panel being drawn on. */
	protected final __PaintingPanel__ panel;
	
	/** Gamepad timer. */
	protected final Timer gamePadTimer;
	
	/** Repainting coordinates. */
	private final int[] _repaint =
		new int[4];
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemCanvas()
	{
		super(UIItemType.CANVAS);
		
		final __PaintingPanel__ panel = new __PaintingPanel__(this);
		this.panel = panel;
		
		// Setup key handler with optional controller support
		NativeGameController controller = NativeGameController.instance();
		HandleKeyEvents keyHandler = new HandleKeyEvents(this,
			controller);
		
		// Add basic listener
		panel.addComponentListener(new HandleComponentEvents(this));
		panel.addKeyListener(keyHandler);
		
		// If there is a game controller we need to poll it for events
		if (controller != null)
		{
			// Setup timer, at 60 FPS which is pretty standard
			Timer timer = new Timer(16, keyHandler);
			timer.setRepeats(true);
			timer.setDelay(16);
			timer.setInitialDelay(16);
			
			// Store for later usage
			this.gamePadTimer = timer;
			
			// Start it
			timer.start();
		}
		else
			this.gamePadTimer = null;
		
		// We control all the drawn pixels here
		panel.setOpaque(true);
		
		// Allow this to be focused, so it can have key events within
		panel.setFocusable(true);
		panel.setRequestFocusEnabled(true);
		panel.setFocusTraversalKeysEnabled(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JPanel component()
	{
		return this.panel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void deletePost()
	{
		if (this.gamePadTimer != null)
			this.gamePadTimer.stop();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@Override
	public void property(int __intProp, int __sub, int __val)
		throws MLECallError
	{
		switch (__intProp)
		{
				// Request repaint of canvas
			case UIWidgetProperty.INT_SIGNAL_REPAINT:
				int[] repaint = this._repaint;
				switch (__val & UISpecialCode.REPAINT_KEY_MASK)
				{
					case UISpecialCode.REPAINT_EXECUTE:
						this.panel.repaint(repaint[0], repaint[1],
							repaint[2], repaint[3]);
						break;
					
					case UISpecialCode.REPAINT_KEY_X:
						repaint[0] = __val & UISpecialCode.REPAINT_VALUE_MASK;
						break;
					
					case UISpecialCode.REPAINT_KEY_Y:
						repaint[1] = __val & UISpecialCode.REPAINT_VALUE_MASK;
						break;
					
					case UISpecialCode.REPAINT_KEY_WIDTH:
						repaint[2] = __val & UISpecialCode.REPAINT_VALUE_MASK;
						break;
					
					case UISpecialCode.REPAINT_KEY_HEIGHT:
						repaint[3] = __val & UISpecialCode.REPAINT_VALUE_MASK;
						break;
					
					default:
						throw new MLECallError(
							"Bad repaint signal: " + __val);
				}
				break;
			
				// Set focus on this canvas
			case UIWidgetProperty.INT_SIGNAL_FOCUS:
				Debugging.debugNote("Requesting Canvas focus...");
			
				this.panel.requestFocusInWindow();
				break;
			
			default:
				throw new MLECallError("Unknown property: " + __intProp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@Override
	public void property(int __strProp, int __sub, String __newValue)
	{
		throw Debugging.todo();
	}
}
