// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.jvm.javase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.launch.AbstractConsoleView;
import net.multiphasicapps.squirreljme.launch.event.EventKind;
import net.multiphasicapps.squirreljme.launch.event.EventQueue;
import net.multiphasicapps.squirreljme.launch.event.KeyChars;

/**
 * This provides a swing console view.
 *
 * @since 2016/05/14
 */
public class SwingConsoleView
	extends AbstractConsoleView
{
	/** Icons for the console. */
	public static final List<Image> ICONS;
	
	/** The frame which displays the console graphics. */
	protected final JFrame frame;
	
	/** The character view. */
	protected final CharacterView view;
	
	/** The font to draw with. */
	protected final Font font;
	
	/** The font width and height. */
	protected final int fontw, fonth;
	
	/** Graphics configuration for the best image selection. */
	protected final GraphicsConfiguration config;
	
	/** The event queue to use. */
	protected final EventQueue eventqueue;
	
	/** The precomposed image buffer. */
	private volatile BufferedImage _buffer;
	
	/** Graphics for the image buffer. */
	private volatile Graphics2D _gfx;
	
	/**
	 * Initializes the icon set.
	 *
	 * @since 2016/05/15
	 */
	static
	{
		// Setup target
		List<Image> icos = new ArrayList<>();
		
		// Look for resources
		BufferedImage base;
		try (InputStream is = new HexInputStream(new InputStreamReader(
			SwingConsoleView.class.getResourceAsStream("icon.hex"), "utf-8")))
		{
			base = ImageIO.read(is);
		}
		
		// The image will just be blank then
		catch (IOException e)
		{
			base = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
		
		// Add the base image always
		icos.add(base);
		
		// Lock in
		ICONS = Collections.<Image>unmodifiableList(icos);
	}
	
	/**
	 * Initializes the swing console view.
	 *
	 * @param __eq The event queue where events should be placed into.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public SwingConsoleView(EventQueue __eq)
		throws NullPointerException
	{
		// Check
		if (__eq == null)
			throw new NullPointerException("NARG");
		
		// Set
		eventqueue = __eq;
		
		// Setup the console view frame
		JFrame frame = new JFrame("SquirrelJME");
		this.frame = frame;
		
		// Setup icons
		frame.setIconImages(ICONS);
		
		// Make it exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Get the device graphics configuration
		GraphicsEnvironment ge = GraphicsEnvironment.
			getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		this.config = gc;
		
		// Create an initial 1x1 image to determine the size of the font
		BufferedImage toy = gc.createCompatibleImage(1, 1);
		Graphics2D toygfx = toy.createGraphics();
		
		// Use monospaced font
		Font font = Font.decode(Font.MONOSPACED);
		this.font = font;
		toygfx.setFont(font);
		
		// Get font character size
		FontMetrics fm = toygfx.getFontMetrics();
		int cw = fm.charWidth('S'),
			ch = fm.getHeight();
		this.fontw = cw;
		this.fonth = ch;
		
		// Setup character view
		CharacterView view = new CharacterView();
		this.view = view;
		
		// Add to the frame
		frame.add(view);
		
		// Handle keyboard/mouse events
		view.addKeyListener(view);
		view.addMouseListener(view);
		view.addMouseMotionListener(view);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public int defaultColumns()
	{
		return 80;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public int defaultRows()
	{
		return 24;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public void displayConsole()
	{
		// Has the console changed?
		if (hasChanged())
		{
			// Determine the size of the terminal and get the details
			int cw = this.fontw, ch = this.fonth;
			int tc, tr, tw, th, cells;
			char[] chars;
			byte[] attrs;
			synchronized (lock)
			{
				tc = getColumns();
				tr = getRows();
				chars = rawChars();
				attrs = rawAttributes();
			}
			
			// Width and height
			cells = tc * tr;
			tw = tc * cw;
			th = tr * ch;
			
			// Need to (re-)create the buffer?
			BufferedImage buffer = this._buffer;
			Graphics2D gfx = this._gfx;
			if (buffer == null || buffer.getWidth() != tw ||
				buffer.getHeight() != th)
			{
				this._buffer = buffer = this.config.createCompatibleImage(tw,
					th);
				this._gfx = gfx = (Graphics2D)buffer.getGraphics();
			}
			
			// Use monospaced font
			gfx.setFont(this.font);
			
			// Wipe over the entire background
			gfx.setColor(Color.BLACK);
			gfx.fillRect(0, 0, tw, th);
			
			// Draw using a single loop
			int head = tw - cw;
			for (int i = cells - 1, dx = head, dy = th; i >= 0; i--, dx -= cw)
			{
				// Previous row?
				if (dx < 0)
				{
					dx = head;
					dy -= ch;
				}
				
				// Get character data and attribute here
				char c = chars[i];
				byte a = attrs[i];
				
				// Draw new background colors over black
				
				// Do not bother drawing control and space characters
				if (c > ' ')
				{
					gfx.setColor(Color.LIGHT_GRAY);
					gfx.drawChars(chars, i, 1, dx, dy);
				}
			}
			
			// Force redraw of the console display
			this.frame.repaint();
		}
	}
	
	/**
	 * Makes the console view visible.
	 *
	 * @since 2016/05/14
	 */
	public void setVisible()
	{
		// Make it shown
		this.frame.setVisible(true);
		
		// Center on screen
		this.frame.setLocationRelativeTo(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	protected void sizeChanged()
	{
		// Get the new size
		int cols = getColumns();
		int rows = getRows();
		
		// Fix the size
		CharacterView view = this.view;
		if (view != null)
			view.__fixSize(cols, rows);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public boolean supportsSize(int __c, int __r)
	{
		// Supports terminals of any size, provided they are at least one
		// by one.
		return __c >= 1 && __r >= 1;
	}
	
	/**
	 * Converts an AWT key event to a character.
	 *
	 * @param __typed If {@code true} then the key was typed.
	 * @param __e The event.
	 * @return The converted key code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/15
	 */	
	private char __keyToChar(boolean __typed, KeyEvent __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// If typed, use the directly typed character
		if (__typed)
		{
			char rv = __e.getKeyChar();
			
			// Only use the given typed key if it was defined
			if (rv != KeyEvent.CHAR_UNDEFINED)
				return rv;
		}
		
		// Depends on the key code
		int vk = __e.getKeyCode();
		switch (vk)
		{
			case KeyEvent.VK_0:	return '0';
			case KeyEvent.VK_1:	return '1';
			case KeyEvent.VK_2:	return '2';
			case KeyEvent.VK_3:	return '3';
			case KeyEvent.VK_4:	return '4';
			case KeyEvent.VK_5:	return '5';
			case KeyEvent.VK_6:	return '6';
			case KeyEvent.VK_7:	return '7';
			case KeyEvent.VK_8:	return '8';
			case KeyEvent.VK_9:	return '9';
			case KeyEvent.VK_A:	return 'A';
			case KeyEvent.VK_ACCEPT:	return KeyChars.ACCEPT;
			case KeyEvent.VK_ADD:	return KeyChars.NUMPAD_ADD;
			case KeyEvent.VK_AGAIN:	return KeyChars.AGAIN;
			case KeyEvent.VK_ALL_CANDIDATES:	return KeyChars.ALL_CANDIDATES;
			case KeyEvent.VK_ALPHANUMERIC:	return KeyChars.ALPHANUMERIC;
			case KeyEvent.VK_ALT:	return KeyChars.LEFT_ALT;
			case KeyEvent.VK_ALT_GRAPH:	return KeyChars.ALT_GRAPH;
			case KeyEvent.VK_AMPERSAND:	return '&';
			case KeyEvent.VK_ASTERISK:	return '*';
			case KeyEvent.VK_AT:	return '@';
			case KeyEvent.VK_B:	return 'B';
			case KeyEvent.VK_BACK_QUOTE:	return '`';
			case KeyEvent.VK_BACK_SLASH:	return '\\';
			case KeyEvent.VK_BACK_SPACE:	return KeyChars.BACK_SPACE;
			case KeyEvent.VK_BEGIN:	return KeyChars.BEGIN;
			case KeyEvent.VK_BRACELEFT:	return '{';
			case KeyEvent.VK_BRACERIGHT:	return '}';
			case KeyEvent.VK_C:	return 'C';
			case KeyEvent.VK_CANCEL:	return KeyChars.CANCEL;
			case KeyEvent.VK_CAPS_LOCK:	return KeyChars.CAPS_LOCK;
			case KeyEvent.VK_CIRCUMFLEX:	return '^';
			case KeyEvent.VK_CLEAR:	return KeyChars.CLEAR;
			case KeyEvent.VK_CLOSE_BRACKET:	return ']';
			case KeyEvent.VK_CODE_INPUT:	return KeyChars.CODE_INPUT;
			case KeyEvent.VK_COLON:	return ':';
			case KeyEvent.VK_COMMA:	return ',';
			case KeyEvent.VK_COMPOSE:	return KeyChars.COMPOSE;
			case KeyEvent.VK_CONTEXT_MENU:	return KeyChars.CONTEXT_MENU;
			case KeyEvent.VK_CONTROL:	return KeyChars.LEFT_CONTROL;
			case KeyEvent.VK_CONVERT:	return KeyChars.CONVERT;
			case KeyEvent.VK_COPY:	return KeyChars.COPY;
			case KeyEvent.VK_CUT:	return KeyChars.CUT;
			case KeyEvent.VK_D:	return 'D';
			case KeyEvent.VK_DEAD_ABOVEDOT:	return KeyChars.DEAD_ABOVEDOT;
			case KeyEvent.VK_DEAD_ABOVERING:	return KeyChars.DEAD_ABOVERING;
			case KeyEvent.VK_DEAD_ACUTE:	return KeyChars.DEAD_ACUTE;
			case KeyEvent.VK_DEAD_BREVE:	return KeyChars.DEAD_BREVE;
			case KeyEvent.VK_DEAD_CARON:	return KeyChars.DEAD_CARON;
			case KeyEvent.VK_DEAD_CEDILLA:	return KeyChars.DEAD_CEDILLA;
			case KeyEvent.VK_DEAD_CIRCUMFLEX:	return KeyChars.DEAD_CIRCUMFLEX;
			case KeyEvent.VK_DEAD_DIAERESIS:	return KeyChars.DEAD_DIAERESIS;
			case KeyEvent.VK_DEAD_DOUBLEACUTE:	return KeyChars.DEAD_DOUBLEACUTE;
			case KeyEvent.VK_DEAD_GRAVE:	return KeyChars.DEAD_GRAVE;
			case KeyEvent.VK_DEAD_IOTA:	return KeyChars.DEAD_IOTA;
			case KeyEvent.VK_DEAD_MACRON:	return KeyChars.DEAD_MACRON;
			case KeyEvent.VK_DEAD_OGONEK:	return KeyChars.DEAD_OGONEK;
			case KeyEvent.VK_DEAD_SEMIVOICED_SOUND:	return KeyChars.DEAD_SEMIVOICED_SOUND;
			case KeyEvent.VK_DEAD_TILDE:	return KeyChars.DEAD_TILDE;
			case KeyEvent.VK_DEAD_VOICED_SOUND:	return KeyChars.DEAD_VOICED_SOUND;
			case KeyEvent.VK_DECIMAL:	return KeyChars.NUMPAD_DECIMAL;
			case KeyEvent.VK_DELETE:	return KeyChars.DELETE;
			case KeyEvent.VK_DIVIDE:	return KeyChars.NUMPAD_DIVIDE;
			case KeyEvent.VK_DOLLAR:	return '$';
			case KeyEvent.VK_DOWN:	return KeyChars.DOWN;
			case KeyEvent.VK_E:	return 'E';
			case KeyEvent.VK_END:	return KeyChars.END;
			case KeyEvent.VK_ENTER:	return KeyChars.ENTER;
			case KeyEvent.VK_EQUALS:	return KeyChars.NUMPAD_ENTER;
			case KeyEvent.VK_ESCAPE:	return KeyChars.ESCAPE;
			case KeyEvent.VK_EURO_SIGN:	return (char)0x20AC;
			case KeyEvent.VK_EXCLAMATION_MARK:	return '!';
			case KeyEvent.VK_F:	return 'F';
			
			case KeyEvent.VK_F1:
			case KeyEvent.VK_F2:
			case KeyEvent.VK_F3:
			case KeyEvent.VK_F4:
			case KeyEvent.VK_F5:
			case KeyEvent.VK_F6:
			case KeyEvent.VK_F7:
			case KeyEvent.VK_F8:
			case KeyEvent.VK_F9:
			case KeyEvent.VK_F10:
			case KeyEvent.VK_F11:
			case KeyEvent.VK_F12:
			case KeyEvent.VK_F13:
			case KeyEvent.VK_F14:
			case KeyEvent.VK_F15:
			case KeyEvent.VK_F16:
			case KeyEvent.VK_F17:
			case KeyEvent.VK_F18:
			case KeyEvent.VK_F19:
			case KeyEvent.VK_F20:
			case KeyEvent.VK_F21:
			case KeyEvent.VK_F22:
			case KeyEvent.VK_F23:
			case KeyEvent.VK_F24:
				return (char)(KeyChars.FUNCTION_1 + (vk - KeyEvent.VK_F24));
				
			case KeyEvent.VK_FINAL:	return KeyChars.FINAL;
			case KeyEvent.VK_FIND:	return KeyChars.FIND;
			case KeyEvent.VK_FULL_WIDTH:	return KeyChars.FULL_WIDTH;
			case KeyEvent.VK_G:	return 'G';
			case KeyEvent.VK_GREATER:	return '>';
			case KeyEvent.VK_H:	return 'H';
			case KeyEvent.VK_HALF_WIDTH:	return KeyChars.HALF_WIDTH;
			case KeyEvent.VK_HELP:	return KeyChars.HELP;
			case KeyEvent.VK_HIRAGANA:	return KeyChars.HIRAGANA;
			case KeyEvent.VK_HOME:	return KeyChars.HOME;
			case KeyEvent.VK_I:	return 'I';
			case KeyEvent.VK_INPUT_METHOD_ON_OFF:	return KeyChars.INPUT_METHOD_ON_OFF;
			case KeyEvent.VK_INSERT:	return KeyChars.INSERT;
			case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:	return (char)0x00A1;
			case KeyEvent.VK_J:	return 'J';
			case KeyEvent.VK_JAPANESE_HIRAGANA:	return KeyChars.JAPANESE_HIRAGANA;
			case KeyEvent.VK_JAPANESE_KATAKANA:	return KeyChars.JAPANESE_KATAKANA;
			case KeyEvent.VK_JAPANESE_ROMAN:	return KeyChars.JAPANESE_ROMAN;
			case KeyEvent.VK_K:	return 'K';
			case KeyEvent.VK_KANA:	return KeyChars.KANA;
			case KeyEvent.VK_KANA_LOCK:	return KeyChars.KANA_LOCK;
			case KeyEvent.VK_KANJI:	return KeyChars.KANJI;
			case KeyEvent.VK_KATAKANA:	return KeyChars.KATAKANA;
			case KeyEvent.VK_KP_DOWN:	return KeyChars.KP_DOWN;
			case KeyEvent.VK_KP_LEFT:	return KeyChars.KP_LEFT;
			case KeyEvent.VK_KP_RIGHT:	return KeyChars.KP_RIGHT;
			case KeyEvent.VK_KP_UP:	return KeyChars.KP_UP;
			case KeyEvent.VK_L:	return 'L';
			case KeyEvent.VK_LEFT:	return KeyChars.LEFT;
			case KeyEvent.VK_LEFT_PARENTHESIS:	return '(';
			case KeyEvent.VK_LESS:	return '<';
			case KeyEvent.VK_M:	return 'M';
			case KeyEvent.VK_META:	return KeyChars.META;
			case KeyEvent.VK_MINUS:	return '-';
			case KeyEvent.VK_MODECHANGE:	return KeyChars.MODECHANGE;
			case KeyEvent.VK_MULTIPLY:	return KeyChars.NUMPAD_MULTIPLY;
			case KeyEvent.VK_N:	return 'N';
			case KeyEvent.VK_NONCONVERT:	return KeyChars.NONCONVERT;
			case KeyEvent.VK_NUMBER_SIGN:	return '#';
			case KeyEvent.VK_NUM_LOCK:	return KeyChars.NUM_LOCK;
			case KeyEvent.VK_NUMPAD0:	return KeyChars.NUMPAD_0;
			case KeyEvent.VK_NUMPAD1:	return KeyChars.NUMPAD_1;
			case KeyEvent.VK_NUMPAD2:	return KeyChars.NUMPAD_2;
			case KeyEvent.VK_NUMPAD3:	return KeyChars.NUMPAD_3;
			case KeyEvent.VK_NUMPAD4:	return KeyChars.NUMPAD_4;
			case KeyEvent.VK_NUMPAD5:	return KeyChars.NUMPAD_5;
			case KeyEvent.VK_NUMPAD6:	return KeyChars.NUMPAD_6;
			case KeyEvent.VK_NUMPAD7:	return KeyChars.NUMPAD_7;
			case KeyEvent.VK_NUMPAD8:	return KeyChars.NUMPAD_8;
			case KeyEvent.VK_NUMPAD9:	return KeyChars.NUMPAD_9;
			case KeyEvent.VK_O:	return 'O';
			case KeyEvent.VK_OPEN_BRACKET:	return '[';
			case KeyEvent.VK_P:	return 'P';
			case KeyEvent.VK_PAGE_DOWN:	return KeyChars.PAGE_DOWN;
			case KeyEvent.VK_PAGE_UP:	return KeyChars.PAGE_UP;
			case KeyEvent.VK_PASTE:	return KeyChars.PASTE;
			case KeyEvent.VK_PAUSE:	return KeyChars.PAUSE;
			case KeyEvent.VK_PERIOD:	return '.';
			case KeyEvent.VK_PLUS:	return '+';
			case KeyEvent.VK_PREVIOUS_CANDIDATE:	return KeyChars.PREVIOUS_CANDIDATE;
			case KeyEvent.VK_PRINTSCREEN:	return KeyChars.PRINTSCREEN;
			case KeyEvent.VK_PROPS:	return KeyChars.PROPS;
			case KeyEvent.VK_Q:	return 'Q';
			case KeyEvent.VK_QUOTE:	return '\'';
			case KeyEvent.VK_QUOTEDBL:	return '"';
			case KeyEvent.VK_R:	return 'R';
			case KeyEvent.VK_RIGHT:	return KeyChars.RIGHT;
			case KeyEvent.VK_RIGHT_PARENTHESIS:	return ')';
			case KeyEvent.VK_ROMAN_CHARACTERS:	return KeyChars.ROMAN_CHARACTERS;
			case KeyEvent.VK_S:	return 'S';
			case KeyEvent.VK_SCROLL_LOCK:	return KeyChars.SCROLL_LOCK;
			case KeyEvent.VK_SEMICOLON:	return ';';
			case KeyEvent.VK_SEPARATOR:	return KeyChars.NUMPAD_SEPARATOR;
			case KeyEvent.VK_SHIFT:	return KeyChars.LEFT_SHIFT;
			case KeyEvent.VK_SLASH:	return '/';
			case KeyEvent.VK_SPACE:	return ' ';
			case KeyEvent.VK_STOP:	return KeyChars.STOP;
			case KeyEvent.VK_SUBTRACT:	return KeyChars.NUMPAD_SUBTRACT;
			case KeyEvent.VK_T:	return 'T';
			case KeyEvent.VK_TAB:	return KeyChars.TAB;
			case KeyEvent.VK_U:	return 'U';
			case KeyEvent.VK_UNDERSCORE:	return '_';
			case KeyEvent.VK_UNDO:	return KeyChars.UNDO;
			case KeyEvent.VK_UP:	return KeyChars.UP;
			case KeyEvent.VK_V:	return 'V';
			case KeyEvent.VK_W:	return 'W';
			case KeyEvent.VK_WINDOWS:	return KeyChars.LOGO;
			case KeyEvent.VK_X:	return 'X';
			case KeyEvent.VK_Y:	return 'Y';
			case KeyEvent.VK_Z:	return 'Z';
			
				// Unknown
			default:
				return (char)0x0000;
		}
	}
	
	/**
	 * Pushes a key event.
	 *
	 * @param __k The event kind.
	 * @param __e The event data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/15
	 */
	private void __pushKeyEvent(EventKind __k, KeyEvent __e)
		throws NullPointerException
	{
		// Check
		if (__k == null || __e == null)
			throw new NullPointerException("NARG");
		
		// Get the event queue
		EventQueue eventqueue = this.eventqueue;
		
		// Depends on the event
		switch (__k)
		{
				// Pressed
			case KEY_PRESSED:
				eventqueue.postKeyPressed(0, __keyToChar(false, __e));
				break;
				
				// Released
			case KEY_RELEASED:
				eventqueue.postKeyReleased(0, __keyToChar(false, __e));
				break;
				
				// Typed
			case KEY_TYPED:
				eventqueue.postKeyTyped(0, __keyToChar(true, __e));
				break;
			
				// Unknown
			default:
				break;
		}
	}
	
	/**
	 * This is a character view which shows the terminal text.
	 *
	 * @since 2016/05/14
	 */
	@SuppressWarnings({"serial"})
	public class CharacterView
		extends JPanel
		implements KeyListener, MouseListener, MouseMotionListener
	{
		/** Has the view size been corrected? */
		private volatile boolean _fixed;
		
		/**
		 * Initializes the character view.
		 *
		 * @since 2016/05/14
		 */
		private CharacterView()
		{
			// Allow the character view to get focused on, mouse and keyboard
			// wise. Also allow characters such as Tab to be pressed also.
			setFocusable(true);
			setRequestFocusEnabled(true);
			setFocusTraversalKeysEnabled(false);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void keyPressed(KeyEvent __e)
		{
			__pushKeyEvent(EventKind.KEY_PRESSED, __e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void keyReleased(KeyEvent __e)
		{
			__pushKeyEvent(EventKind.KEY_RELEASED, __e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void keyTyped(KeyEvent __e)
		{
			__pushKeyEvent(EventKind.KEY_TYPED, __e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseClicked(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseDragged(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseEntered(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseExited(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseMoved(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mousePressed(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/15
		 */
		@Override
		public void mouseReleased(MouseEvent __e)
		{
			//System.err.println(__e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/14
		 */
		@Override
		public void paintComponent(Graphics __g)
		{
			// Call super paint
			super.paintComponent(__g);
			
			// Fix it?
			if (!this._fixed)
			{
				SwingConsoleView.this.sizeChanged();
				frame.setLocationRelativeTo(null);
			}
			
			// Draw precomposed console image
			BufferedImage bi = SwingConsoleView.this._buffer;
			if (bi != null)
				__g.drawImage(bi, 0, 0, null);
		}
		
		/**
		 * Fixes the panel size.
		 *
		 * @param __c Column count.
		 * @param __r Row count.
		 * @since 2016/05/14
		 */
		void __fixSize(int __c, int __r)
		{
			// Setup dimension
			int nw = __c * Math.max(1, fontw);
			int nh = __r * Math.max(1, fonth);
			Dimension d = new Dimension(nw, nh);
			
			// Set everything
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			setSize(d);
			
			// Pack it
			frame.pack();
			frame.validate();
			
			// Set
			this._fixed = true;
		}
	}
}

