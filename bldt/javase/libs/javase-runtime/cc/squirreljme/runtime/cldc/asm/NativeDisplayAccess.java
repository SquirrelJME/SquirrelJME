// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.javase.lcdui.ColorInfo;
import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Display;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import net.multiphasicapps.io.MIMEFileDecoder;

/**
 * Java SE implementation of the native display system using Swing.
 *
 * @since 2018/11/16
 */
public final class NativeDisplayAccess
{
	/** The number of parameters available. */
	public static final int NUM_PARAMETERS =
		8;
	
	/** The pixel format. */
	public static final int PARAMETER_PIXELFORMAT =
		0;
	
	/** The buffer width. */
	public static final int PARAMETER_BUFFERWIDTH =
		1;
	
	/** The buffer height. */
	public static final int PARAMETER_BUFFERHEIGHT =
		2;
	
	/** Alpha channel is used? */
	public static final int PARAMETER_ALPHA =
		3;
	
	/** Buffer pitch. */
	public static final int PARAMETER_PITCH =
		4;
	
	/** Buffer offset. */
	public static final int PARAMETER_OFFSET =
		5;
	
	/** Virtual X offset. */
	public static final int PARAMETER_VIRTXOFF =
		6;
	
	/** Virtual Y offset. */
	public static final int PARAMETER_VIRTYOFF =
		7;
	
	/** The callback for events. */
	private static volatile NativeDisplayEventCallback _CALLBACK;
	
	/** The frame to display. */
	private static volatile JFrame _frame;
	
	/** The panel used to display graphics on. */
	private static volatile SwingPanel _panel;
	
	/** State count for this framebuffer. */
	static volatile int _statecount;
	
	/**
	 * Initializes some things
	 *
	 * @since 2018/11/18
	 */
	static
	{
		try
		{
			// But doing this, speed is increased greatly!
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * Initialize and/or reset accelerated graphics operations.
	 *
	 * @param __id The display to initialize for.
	 * @return {@code true} if acceleration is supported.
	 * @since 2018/11/19
	 */
	public static final boolean accelGfx(int __id)
	{
		// Not supported on Swing because graphics operations are pretty
		// fast already
		return false;
	}
	
	/**
	 * Performs accelerated graphics operation.
	 *
	 * @param __id The display ID.
	 * @param __func The function to call.
	 * @param __args Arguments to the operation.
	 * @return The result of the operation.
	 * @since 2018/11/19
	 */
	public static final Object accelGfxFunc(int __id, int __func,
		Object... __args)
	{
		return null;
	}
	
	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public static final int capabilities(int __id)
	{
		return Display.SUPPORTS_INPUT_EVENTS |
			Display.SUPPORTS_TITLE |
			Display.SUPPORTS_ORIENTATION_PORTRAIT |
			Display.SUPPORTS_ORIENTATION_LANDSCAPE;
	}
	
	/**
	 * Requests that the display should be repainted.
	 *
	 * @param __id The display ID.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/12/03
	 */
	public static final void displayRepaint(int __id,
		int __x, int __y, int __w, int __h)
	{
		if (__id != 0)
			return;
		
		NativeDisplayAccess.__panel().repaint(__x, __y, __w, __h);
	}
	
	/**
	 * Returns the object representing the framebuffer data.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer array.
	 * @since 2018/11/18
	 */
	public static final Object framebufferObject(int __id)
	{
		if (__id != 0)
			return null;
		
		return ColorInfo.getArray(NativeDisplayAccess.__panel()._image);
	}
	
	/**
	 * Returns the palette of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The palette of the framebuffer.
	 * @since 2018/11/18
	 */
	public static final int[] framebufferPalette(int __id)
	{
		if (__id != 0)
			return null;
		
		return ColorInfo.getPalette(NativeDisplayAccess.__panel()._image);
	}
	
	/**
	 * Returns the parameters of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer parameters.
	 * @since 2018/11/18
	 */
	public static final int[] framebufferParameters(int __id)
	{
		if (__id != 0)
			return null;
		
		BufferedImage image = NativeDisplayAccess.__panel()._image;
		
		// Build parameters
		int[] rv = new int[NativeDisplayAccess.NUM_PARAMETERS];
		rv[NativeDisplayAccess.PARAMETER_PIXELFORMAT] =
			ColorInfo.PIXEL_FORMAT.ordinal();
		rv[NativeDisplayAccess.PARAMETER_BUFFERWIDTH] = image.getWidth();
		rv[NativeDisplayAccess.PARAMETER_BUFFERHEIGHT] = image.getHeight();
		rv[NativeDisplayAccess.PARAMETER_ALPHA] = 0;
		rv[NativeDisplayAccess.PARAMETER_PITCH] = image.getWidth();
		rv[NativeDisplayAccess.PARAMETER_OFFSET] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTXOFF] = 0;
		rv[NativeDisplayAccess.PARAMETER_VIRTYOFF] = 0;
		
		return rv;
	}
	
	/**
	 * Returns the state count of this framebuffer which is used to detect
	 * when the parameters have changed, where they must all be recalculated
	 * (that is the framebuffer wrapper must be recreated).
	 *
	 * @param __id The display ID.
	 * @return The state count for the framebuffer.
	 * @since 2018/12/02
	 */
	public static final int framebufferStateCount(int __id)
	{
		if (__id != 0)
			return -1;
		
		return NativeDisplayAccess._statecount;
	}
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public static final boolean isUpsideDown(int __id)
	{
		return false;
	}
	
	/**
	 * Returns the number of permanent displays which are currently attached to
	 * the system.
	 *
	 * @return The number of displays attached to the system.
	 * @since 2018/11/16
	 */
	public static final int numDisplays()
	{
		// There is ever only a single display that is supported
		return 1;
	}
	
	/**
	 * Registers the class to be called for when display events are to be
	 * called.
	 *
	 * @param __cb The callback.
	 * @since 2018/12/03
	 */
	public static final void registerEventCallback(
		NativeDisplayEventCallback __cb)
	{
		NativeDisplayAccess._CALLBACK = __cb;
	}
	
	/**
	 * Sets the title of the display.
	 *
	 * @param __id The display ID.
	 * @param __t The title to use.
	 * @since 2018/11/18
	 */
	public static final void setDisplayTitle(int __id, String __t)
	{
		if (__id != 0)
			return;
		
		NativeDisplayAccess.__frame().setTitle(
			(__t == null ? "SquirrelJME" : __t));
	}
	
	/**
	 * Returns the current frame.
	 *
	 * @return The current frame.
	 * @since 2018/11/18
	 */
	static final JFrame __frame()
	{
		// Without the lock, the frame is initialized multiple times
		synchronized (NativeDisplayAccess.class)
		{
			JFrame rv = NativeDisplayAccess._frame;
			if (rv != null)
				return rv;
				
			// Debug
			todo.DEBUG.note("Setting up the frame.");
			
			// Setup frame
			NativeDisplayAccess._frame = (rv = new JFrame());
			
			// Exit when close is pressed
			rv.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			
			// Initial title
			rv.setTitle("SquirrelJME");
			
			// Use a better panel size
			rv.setMinimumSize(new Dimension(160, 160));
			rv.setPreferredSize(new Dimension(640, 480));
			
			// Load icons, for those that exist anyway
			List<Image> icons = new ArrayList<>();
			for (int i : new int[]{8, 16, 24, 32, 48, 64})
			{
				String rcname = String.format("head_%dx%d.png", i, i);
				URL rc = ColorInfo.class.getResource(rcname);
				
				// If it does not exist, try loading MIME data instead
				// The new bootstrap does not decode these
				if (rc == null)
				{
					try (InputStream in = ColorInfo.class.getResourceAsStream(
						rcname + ".__mime"))
					{
						// Still does not exist
						if (in == null)
							continue;
						
						// Decode MIME data
						try (ByteArrayOutputStream baos = new
							ByteArrayOutputStream();
							InputStream inx = new MIMEFileDecoder(in, "utf-8"))
						{
							byte[] buf = new byte[512];
							for (;;)
							{
								int rcx = inx.read(buf);
								
								if (rcx < 0)
									break;
								
								baos.write(buf, 0, rcx);
							}
							
							// Load icon
							icons.add(new ImageIcon(baos.toByteArray()).
								getImage());
						}
					}
					
					// Ignore
					catch (IOException e)
					{
					}
					
					// Go to the next icon
					continue;
				}
				
				// Add the icon
				icons.add(new ImageIcon(rc).getImage());
			}
			rv.setIconImages(icons);
			
			return rv;
		}
	}
	
	/**
	 * Returns the current panel.
	 *
	 * @return The current panel.
	 * @since 2018/11/18
	 */
	static final SwingPanel __panel()
	{
		// Without the lock, the panel is initialized multiple times
		synchronized (NativeDisplayAccess.class)
		{
			SwingPanel rv = NativeDisplayAccess._panel;
			if (rv != null)
				return rv;
			
			// Debug
			todo.DEBUG.note("Setting up the panel.");
			
			// Setup panel
			NativeDisplayAccess._panel = (rv = new SwingPanel());
			
			// Add to the frame
			JFrame frame = NativeDisplayAccess.__frame();
			frame.add(rv);
			
			// Record some events
			frame.addComponentListener(rv);
			frame.addWindowListener(rv);
			frame.addKeyListener(rv);
			frame.addMouseListener(rv);
			frame.addMouseMotionListener(rv);
			
			// Pack the frame
			frame.pack();
			
			// Make the frame visible and set its properties
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			return rv;
		}
	}
	
	/**
	 * The panel used to display graphics on.
	 *
	 * @since 2018/11/18
	 */
	public static final class SwingPanel
		extends JPanel
		implements ActionListener, ComponentListener, KeyListener,
			MouseListener, MouseMotionListener, WindowListener
	{
		/** Resize lock timer. */
		final Timer _resizetimer =
			new Timer(500, this);
		
		/** The image to be displayed. */
		volatile BufferedImage _image =
			ColorInfo.create(Math.max(320, this.getWidth()),
				Math.max(240, this.getHeight()), new Color(0xFFFFFFFF));
		
		/** The last mouse button pressed. */
		volatile int _lastbutton =
			Integer.MIN_VALUE;
		
		/**
		 * Initializes more panel details.
		 *
		 * @since 2018/12/03
		 */
		{
			this._resizetimer.setRepeats(false);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/03
		 */
		@Override
		public void actionPerformed(ActionEvent __e)
		{
			todo.DEBUG.note("Performed resize!");
			
			BufferedImage image = this._image;
			int oldw = image.getWidth(),
				oldh = image.getHeight(),
				xw = this.getWidth(),
				xh = this.getHeight();
		
			// Recreate the image if the size has changed
			if (xw != oldw || xh != oldh)
			{
				this._image = (image = ColorInfo.create(xw, xh,
					new Color(0xFFFFFFFF)));
				NativeDisplayAccess._statecount++;
			}
			
			// Indicate that the size changed
			NativeDisplayAccess._CALLBACK.sizeChanged(0, xw, xh);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void componentHidden(ComponentEvent __e)
		{
			NativeDisplayAccess._CALLBACK.shown(0, 0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void componentMoved(ComponentEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void componentResized(ComponentEvent __e)
		{
			// Call the resize code directly
			if (false)
				this.actionPerformed(null);
				
			// Restart the resize timer so that resizes are not flooding
			// everything
			else
				this._resizetimer.restart();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void componentShown(ComponentEvent __e)
		{
			NativeDisplayAccess._CALLBACK.shown(0, 1);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyPressed(KeyEvent __e)
		{
			NativeDisplayAccess._CALLBACK.keyEvent(0,
				NativeDisplayEventCallback.KEY_PRESSED,
				__KeyMap__.__map(__e), __KeyMap__.__char(__e),
				(int)__e.getWhen());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyReleased(KeyEvent __e)
		{
			NativeDisplayAccess._CALLBACK.keyEvent(0,
				NativeDisplayEventCallback.KEY_RELEASED,
				__KeyMap__.__map(__e), __KeyMap__.__char(__e),
				(int)__e.getWhen());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/01
		 */
		@Override
		public void keyTyped(KeyEvent __e)
		{
			NativeDisplayAccess._CALLBACK.keyEvent(0,
				NativeDisplayEventCallback.KEY_PRESSED,
				__KeyMap__.__map(__e), __KeyMap__.__char(__e),
				(int)__e.getWhen());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseClicked(MouseEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseDragged(MouseEvent __e)
		{
			this.__mouseEvent(__e,
				NativeDisplayEventCallback.POINTER_DRAGGED);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseEntered(MouseEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseExited(MouseEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseMoved(MouseEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mousePressed(MouseEvent __e)
		{
			this.__mouseEvent(__e,
				NativeDisplayEventCallback.POINTER_PRESSED);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/12/02
		 */
		@Override
		public void mouseReleased(MouseEvent __e)
		{
			this.__mouseEvent(__e,
				NativeDisplayEventCallback.POINTER_RELEASED);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		protected void paintComponent(java.awt.Graphics __g)
		{
			// This must always be called
			super.paintComponent(__g);
			
			BufferedImage image = this._image;
			int oldw = image.getWidth(),
				oldh = image.getHeight(),
				xw = this.getWidth(),
				xh = this.getHeight();
		
			// Recreate the image if the size has changed
			if (xw != oldw || xh != oldh)
			{
				this._image = (image = ColorInfo.create(xw, xh,
					new Color(0xFFFFFFFF)));
				NativeDisplayAccess._statecount++;
			}
			
			// Have the display client draw whatever is on this display, but
			// only in the region we are drawing!
			Rectangle rect = __g.getClipBounds();
			NativeDisplayAccess._CALLBACK.paintDisplay(0,
				rect.x, rect.y,
				rect.width, rect.height);
			
			// Draw the backed buffered image
			__g.drawImage(image, 0, 0, xw, xh,
				0, 0, xw, xh, null);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowActivated(WindowEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowClosed(WindowEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowClosing(WindowEvent __e)
		{
			todo.DEBUG.note("Window is closing!");
			
			// Post close event
			NativeDisplayAccess._CALLBACK.exitRequest(0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowDeactivated(WindowEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowDeiconified(WindowEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowIconified(WindowEvent __e)
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/18
		 */
		@Override
		public void windowOpened(WindowEvent __e)
		{
		}
		
		/**
		 * Handles mouse event.
		 *
		 * @param __e The event.
		 * @param __t The event type.
		 * @since 2018/12/02
		 */
		private final void __mouseEvent(MouseEvent __e, int __t)
		{
			int mybutton = __e.getButton(),
				lastbutton = this._lastbutton;
			
			// Depends on the event
			switch (__t)
			{
				case NativeDisplayEventCallback.POINTER_DRAGGED:
					// For some reason in Swing, the mouse being dragged is
					// always button zero, so just send drag event no matter
					// what without any button filtering
					break;
				
				case NativeDisplayEventCallback.POINTER_PRESSED:
					if (lastbutton < 0 || mybutton == lastbutton)
						this._lastbutton = mybutton;
					else
						return;
					break;
				
				case NativeDisplayEventCallback.POINTER_RELEASED:
					if (mybutton == lastbutton)
						this._lastbutton = Integer.MIN_VALUE;
					else
						return;
					break;
			}
			
			// Post event
			NativeDisplayAccess._CALLBACK.pointerEvent(0,
				__t, __e.getX(), __e.getY(), (int)__e.getWhen());
		}
	}
}

