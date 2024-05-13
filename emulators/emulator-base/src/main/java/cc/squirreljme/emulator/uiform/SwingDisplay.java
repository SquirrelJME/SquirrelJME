// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Represents a Swing Display.
 *
 * @since 2020/07/01
 */
@Deprecated
public final class SwingDisplay
	implements UIDisplayBracket
{
	/** The screen instance. */
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", 
		"NonConstantFieldWithUpperCaseName"})
	private static SwingDisplay _DISPLAY;
	
	/** The frame that makes up this display. */
	protected final JFrame frame;
	
	/** The inner display panel. */
	protected final __PaintingPanel__ paintingPanel;
	
	/** The callback for this display. */
	private UIDisplayCallback _callback;
	
	/** The current frame visible on the form. */
	private SwingForm _current;
	
	static
	{
		// We need to poke native binding, so it loads our emulation backend
		NativeBinding.loadedLibraryPath();
		
		try
		{
			// Greatly optimizes speed
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
		catch (Throwable ignored)
		{
		}
	}
	
	/**
	 * Initializes the display.
	 * 
	 * @since 2020/07/01
	 */
	public SwingDisplay()
	{
		JFrame frame = new JFrame();
		
		// Branding!
		frame.setTitle("SquirrelJME");
		
		// Set icon for the application
		try (InputStream in = SwingDisplay.class.getResourceAsStream(
			"icon.png"))
		{
			Image icon = ImageIO.read(in);
			
			frame.setIconImage(icon);
		}
		catch (IOException ignored)
		{
		}
		
		// Use a basic layout for the form
		frame.setLayout(new BorderLayout());
		
		// We will be handling window closing, so disable the button and
		// implement the close logic ourselves
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Add window listener to handle close events and resizing
		frame.addWindowListener(new HandleDisplayWindowEvent(this));
		
		// Add the base canvas panel
		__PaintingPanel__ paintingPanel = new __PaintingPanel__(this);
		paintingPanel.setMinimumSize(new Dimension(240, 320));
		paintingPanel.setPreferredSize(new Dimension(240, 320));
		
		this.frame = frame;
		this.paintingPanel = paintingPanel;
	}
	
	/**
	 * Returns the display callback.
	 * 
	 * @return The display callback.
	 * @since 2023/01/14
	 */
	public UIDisplayCallback callback()
	{
		synchronized (this)
		{
			return this._callback;
		}
	}
	
	/**
	 * Returns the current form being displayed.
	 * 
	 * @return The current form.
	 * @since 2020/07/19
	 */
	public SwingForm current()
	{
		synchronized (this)
		{
			return this._current;
		}
	}
	
	/**
	 * Sets the display callback.
	 * 
	 * @param __callback The callback to set.
	 * @since 2023/01/14
	 */
	public void setCallback(UIDisplayCallback __callback)
	{
		synchronized (this)
		{
			this._callback = __callback;
		}
	}
	
	/**
	 * Shows the given display without a form potentially.
	 * 
	 * @param __show Should the display be shown?
	 * @throws MLECallError If the display could not be shown.
	 * @since 2023/01/14
	 */
	public void show(boolean __show)
		throws MLECallError
	{
		this.__show(__show, null);
	}
	
	/**
	 * Shows the given form on the display.
	 * 
	 * @param __form The form to show.
	 * @since 2020/07/01
	 */
	public void show(SwingForm __form)
		throws MLECallError
	{
		this.__show(__form != null, __form);
	}
	
	/**
	 * Returns the display instance.
	 * 
	 * @return The instance of the display.
	 * @since 2020/07/01
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static SwingDisplay getInstance()
	{
		// Already cached?
		SwingDisplay rv = SwingDisplay._DISPLAY;
		if (rv != null)
			return rv;
		
		// Create it?
		rv = new SwingDisplay();
		SwingDisplay._DISPLAY = rv;
		
		return rv;
	}
	
	/**
	 * Shows or hides the given form.
	 * 
	 * @param __show Should this be shown?
	 * @param __form The form to potentially be shown.
	 * @since 2023/01/14
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	private void __show(boolean __show, SwingForm __form)
	{
		synchronized (this)
		{
			JFrame frame = this.frame;
			
			// The form must not be associated with a display
			if (__form != null)
				synchronized (__form)
				{
					if (__form._display != null)
						throw new MLECallError(
							"Form associated with display.");
				}
			
			// Do nothing if this is already a current form, or if the frame
			// is already visible with no contained form
			SwingForm current = this._current;
			if (__show && (__form != null && current == __form) ||
				(__form == null && frame.isVisible() && current == null))
				return;
			
			// Remove previous form if there is one
			if (current != null)
				synchronized (current)
				{
					// Snip it out
					frame.remove(current.formPanel);
					
					// And clear the linking
					current._display = null;
				}
			
			// Hide the current frame
			if (!__show)
			{
				frame.setVisible(false);
			}
			
			// Make our frame visible, and any optional form
			else
			{
				// Remove all components from the frame and make our form the
				// only one
				frame.setLayout(new BorderLayout());
				if (__form != null)
				{
					frame.remove(this.paintingPanel);
					frame.add(__form.formPanel, BorderLayout.CENTER);
					
					// Set an appropriate title
					if (__form._nextTitle != null)
						frame.setTitle(__form._nextTitle);
				}
				
				// Use blank canvas panel for drawing
				else
					frame.add(this.paintingPanel, BorderLayout.CENTER);
				
				// Minimize space used
				frame.pack();
				
				// If the frame was not already visible then just center it on
				// screen 
				boolean alreadyVisible = frame.isVisible();
				if (!alreadyVisible)
					frame.setLocationRelativeTo(null);
				
				// Now show it
				frame.setVisible(true);
				
				// Currently displayed form
				if (__form != null)
					synchronized (__form)
					{
						this._current = __form;
						__form._display = this;
					}
			}
		}
		
		// Tell the form it was made current
		if (__form != null)
			__form.madeCurrent();
	}
}
