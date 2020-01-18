// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.lcdui.vfb.VirtualFramebuffer;
import java.awt.Dimension;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.multiphasicapps.io.MIMEFileDecoder;

/**
 * This is a framebuffer that uses Java SE's Swing framework.
 *
 * @since 2019/12/28
 */
public final class SwingFramebuffer
{
	/** The singular created instance. */
	private static SwingFramebuffer _INSTANCE;
	
	/** Virtual framebuffer to use. */
	protected final VirtualFramebuffer vfb =
		new VirtualFramebuffer(new DefaultIPCRouter());
	
	/** Main frame for the framebuffer. */
	protected final JFrame frame;
	
	/** The panel for the framebuffer. */
	protected final FramebufferPanel panel;
	
	/**
	 * Sets global Swing settings.
	 *
	 * @since 2018/11/18
	 */
	static
	{
		try
		{
			// Setting this to true greatly increases the speed of the canvas
			// however this breaks on Windows 10 with pointer coordinates
			// being way off when scaling is used. So this must be false.
			JFrame.setDefaultLookAndFeelDecorated(false);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * Initializes the swing framebuffer.
	 *
	 * @since 2020/01/18
	 */
	public SwingFramebuffer()
	{
		// Setup frame and panel
		JFrame frame = new JFrame();
		FramebufferPanel panel = new FramebufferPanel();
		
		// Exit when close is pressed
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Initial title
		frame.setTitle("SquirrelJME");
		
		// Load icons
		try
		{
			frame.setIconImages(SwingFramebuffer.__loadIcons());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Use a better panel size
		frame.setMinimumSize(new Dimension(160, 160));
		frame.setPreferredSize(new Dimension(640, 480));
		
		// Panel becomes part of the frame now
		frame.add(panel);
		
		// Pack the frame
		frame.pack();
		
		// Make the frame visible and set its properties
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Set
		this.frame = frame;
		this.panel = panel;
	}
	
	/**
	 * Returns an instance of the framebuffer.
	 *
	 * @return The framebuffer instance.
	 * @since 2019/12/28
	 */
	public static final SwingFramebuffer instance()
	{
		SwingFramebuffer rv = _INSTANCE;
		if (rv == null)
			_INSTANCE = (rv = new SwingFramebuffer());
		return rv;
	}
	
	/**
	 * Loads the program icons.
	 *
	 * @return The icons.
	 * @throws IOException If the icons could not be read.
	 * @since 2020/01/18
	 */
	private static final List<Image> __loadIcons()
		throws IOException
	{
		List<Image> rv = new ArrayList<>();
		
		// Use a fixed set of sizes
		for (int i : new int[]{8, 16, 24, 32, 48, 64})
		{
			// Format of the icons
			String rcname = String.format("head_%dx%d.png", i, i);
			
			// Icon data that has been loaded
			byte[] icondata = null;
			
			// Load data
			try (InputStream rin = SwingFramebuffer.class.
					getResourceAsStream(rcname);
				InputStream min = SwingFramebuffer.class.
					getResourceAsStream(rcname + ".__mime");
				InputStream in = (rin != null ? rin : (min != null ?
					new MIMEFileDecoder(min, "utf-8") : null)))
			{
				if (in == null)
					continue;
					
				// Read
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
				{
					// Copu
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
					
					// Store
					icondata = baos.toByteArray();
				}
			}
			
			// Use the given data
			if (icondata != null)
				rv.add(new ImageIcon(icondata).getImage());
		}
		
		return rv;
	}
}

