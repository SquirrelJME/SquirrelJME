// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageType;
import net.multiphasicapps.squirreljme.ui.PIDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIMenu;

/**
 * This implemens the internal display in Swing.
 *
 * A display in Swing is mapped to a {@link JFrame}.
 *
 * @since 2016/05/21
 */
public class SwingDisplay
	extends SwingBase
	implements PIDisplay, WindowListener
{
	/** The frame for the display. */
	protected final JFrame frame;
	
	/** The current menu bar being used to display the menu. */
	protected final JMenuBar menubar =
		new JMenuBar();
	
	/**
	 * Initializes the swing display.
	 *
	 * @param __sm The Swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/22
	 */
	public SwingDisplay(SwingManager __sm,
		Reference<? extends UIDisplay> __ref)
	{
		super(__sm, __ref);
		
		// Create the frame
		JFrame frame = new JFrame();
		this.frame = frame;
		
		// The frame may have a close callback event attached to it, so the
		// window cannot be normally closed unles disposed.
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Handle events
		frame.addWindowListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void containeesChanged()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public boolean isVisible()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return this.frame.isVisible();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void setIcon(UIImage __icon)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// No icon to set?
			if (__icon == null)
			{
				this.frame.setIconImage(null);
				return;
			}
			
			// Get the preferred icon sizes to use
			int[] pis = platformManager().preferredIconSizes();
			int pisn = pis.length;
			
			// Load all icons
			List<Image> icons = new ArrayList<>();
			for (int i = 0; i < pisn; i += 2)
			{
				// Size
				int w = pis[i];
				int h = pis[i + 1];
				
				// Find an internal buffered image
				try
				{
					// Is there an image available?
					ImageData id = __icon.getImage(w, h, ImageType.INT_ARGB,
						false);
					
					// Ignore if not found
					if (id == null)
						continue;
					
					// Load internal image
					BufferedImage bi = platformManager().
						imageDataToBufferedImage(id);
					
					// Add it
					icons.add(bi);
				}
			
				// Ignore
				catch (UIException e)
				{
				}
			}
			
			// Set the icon for the frame
			this.frame.setIconImages(icons);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 201605/23
	 */
	@Override
	public void setMenu(UIMenu __menu)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Get the bar
			JMenuBar menubar = this.menubar;
			JFrame frame = this.frame;
			
			// Clearing the menu?
			if (__menu == null)
			{
				// Clear the menu bar
				frame.setJMenuBar(null);
				menubar.removeAll();
				
				// Done
				return;
			}
			
			// Get the internal bar representation
			SwingMenu sm = platformManager().
				<SwingMenu>internal(SwingMenu.class, __menu);
			
			// Do nothing if it was collected or similar
			if (sm == null)
				return;
			
			// Create the single menu
			JMenu symmenu = (JMenu)sm.__getJMenuItem();
			
			// Clear the old menu and use the new one
			menubar.removeAll();
			menubar.add(symmenu);
			
			// Set the frame bar
			frame.setJMenuBar(menubar);
			
			// Invalidate the frame to update everything
			frame.revalidate();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void setTitle(String __nt)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			this.frame.setTitle((__nt == null ? "" : __nt));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void setVisible(boolean __vis)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Make it shown
			JFrame frame = this.frame;
			frame.setVisible(__vis);
			
			// Pack the frame so it fits better
			frame.pack();
			
			// Center it
			frame.setLocationRelativeTo(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowActivated(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowClosed(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowClosing(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowDeactivated(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowDeiconified(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowIconified(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowOpened(WindowEvent __e)
	{
	}
}

