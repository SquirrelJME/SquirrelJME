// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import org.intellij.lang.annotations.Language;

/**
 * This manages and plays actual media.
 *
 * @since 2025/12/27
 */
public class MediaPlayer
	extends Canvas
{
	/** The height of the progress bar. */
	public static final int BAR_HEIGHT =
		24;
	
	/** The padding of the progress bar. */
	public static final int BAR_PADDING =
		8;
	
	/** The binder this is under. */
	private final Reference<Binder> _binder;
	
	/** The currently playing media. */
	private volatile Player _player;
	
	/**
	 * Initializes the media player.
	 *
	 * @param __binder The associated binder.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	public MediaPlayer(Reference<Binder> __binder)
		throws NullPointerException
	{
		if (__binder == null)
			throw new NullPointerException("NARG");
		
		this._binder = __binder;
	}
	
	/**
	 * Destroys the currently playing media.
	 *
	 * @since 2025/12/27
	 */
	public void destroy()
	{
		synchronized (this)
		{
			// Make sure it is stopped first
			this.stop();
			
			// Deallocate and remove the player
			Player player = this._player;
			if (player != null)
			{
				player.deallocate();
				player.close();
			}
		}
	}
	
	@Override
	protected void keyPressed(int __code)
	{
		// Repaint the display
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected void keyRepeated(int __code)
	{
		// Just treat as a key press
		this.keyPressed(__code);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected void paint(Graphics __g)
	{
		// Is there a player here?
		Player player;
		synchronized (this)
		{
			player = this._player;
		}
		
		// Top media info
		int ix = MediaPlayer.BAR_PADDING;
		int iy = MediaPlayer.BAR_PADDING;
		
		// Font is needed for marquee effect
		Font font = __g.getFont();
		
		// How big is this canvas?
		int w = this.getWidth();
		int h = this.getHeight();
		
		// Get the address the media is at
		String url = null;
		Binder binder = this._binder.get();
		if (binder != null)
			url = binder.connection.getURL();
		
		// Unknown URL? Just point to unknown
		if (url == null)
			url = "<unknown>";
		
		// How long is the URL?
		int sw = (font != null ? font.stringWidth(url) : 1) +
			MediaPlayer.BAR_PADDING;
		
		// Marque effect for the string
		long baseTime = (int)Math.abs((System.nanoTime() /
			(250_000_000L / 16)));
		
		// Add a time penalty before the URL scrolls to the right
		int pn = (int)(baseTime % (sw * 4));
		int bn = (int)(baseTime % sw);
		
		// Determine left side coordinate
		// If the penalty is before the base, do not scroll
		int mx;
		if (pn >= (bn * 2))
			mx = ix;
		
		// Otherwise, start scrolling to the right
		else
		{
			mx = -bn;
			if (mx < -(sw - w))
				mx = -(sw - w);
		}
		
		// Draw the URL showing where the media is
		__g.drawString(url, mx, iy, 0);
		
		// Where are we at and how long is this media?
		long trk = (player != null ? player.getMediaTime() : 0);
		long dur = (player != null ? player.getDuration() : 0);
		
		// Determine base bar coordinates, keep some extra room from the
		// bottom just in case
		int bx = MediaPlayer.BAR_PADDING;
		int by = h - (MediaPlayer.BAR_HEIGHT * 3);
		int bw = w - (MediaPlayer.BAR_PADDING * 2);
		int bh = MediaPlayer.BAR_HEIGHT;
		
		// Back of progress bar
		__g.setColor(0xC3C3C3);
		__g.fillRect(bx, by,
			bw, bh);
		
		// Determine base position for where the bar is filled in
		int fx = MediaPlayer.BAR_PADDING;
		int fw = (dur <= 0 ? 0 :
			(int)((float)bw * ((float)trk / (float)dur)));
		
		// Front of progress bar
		__g.setColor(0xFF7900);
		__g.fillRect(fx, by,
			fw, bh);
		
		// Get the current font
		int fh = (font != null ? font.getHeight() : 12);
		
		// Draw the media time
		__g.setColor(0x000000);
		this.paintTime(__g, trk,
			fx + bw, by - (fh * 2));
		this.paintTime(__g, dur,
			fx + bw, by - fh);
	}
	
	/**
	 * Paints the current time.
	 *
	 * @param __g The graphics to draw in.
	 * @param __micros The microsecond time.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @since 2026/01/01
	 */
	public void paintTime(Graphics __g, long __micros, int __x, int __y)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		// Add microseconds
		long mod = __micros % 1_000_000;
		long div = __micros / 1_000_000;
		sb.append(String.format("%06d\u00B5s", mod));
		
		// Add seconds
		if (div > 0)
		{
			mod = div % 60;
			div = div / 60;
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02ds", mod));
		}
		
		// Add minutes
		if (div > 0)
		{
			mod = div % 60;
			div = div / 60;
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02dm", mod));
		}
		
		// Add hours
		if (div > 0)
		{
			if (sb.length() > 0)
				sb.insert(0, ' ');
			sb.insert(0, String.format("%02dh", div));
		}
		
		// Draw the time
		__g.drawString(sb.toString(), __x, __y,
			Graphics.RIGHT);
	}
	
	/**
	 * Plays the given media.
	 *
	 * @param __conn The connection to play.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	public MediaPlayer play(InputConnection __conn)
		throws NullPointerException
	{
		if (__conn == null)
			throw new NullPointerException("NARG");
		
		// Load the media in
		Player player;
		try (InputStream in = __conn.openInputStream())
		{
			// Do we know or can we guess the content type?
			@Language("mime-type-reference")
			String contentType = null;
			if (__conn instanceof HttpConnection)
				contentType = ((HttpConnection)__conn).getType();
			else if (__conn instanceof FileConnection)
				contentType = ContentTypeUtil.guessByPath(
					((FileConnection)__conn).getPath());
			
			// Setup player to play the data with
			player = Manager.createPlayer(in, contentType);
		}
		
		// Could not load this media
		catch (IOException|MediaException __e)
		{
			// Print error
			__e.printStackTrace();
			
			// Do not change anything
			return this;
		}
		
		// Do use this player
		synchronized (this)
		{
			// Destroy any existing player
			this.destroy();
			
			// Use this player
			this._player = player;
				
			// Implicit play
			try
			{
				player.start();
			}
			catch (MediaException __e)
			{
				__e.printStackTrace();
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected void pointerDragged(int __x, int __y)
	{
		// Repaint the display
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/01
	 */
	@Override
	protected void pointerPressed(int __x, int __y)
	{
		// Repaint the display
		this.repaint();
	}
	
	/**
	 * Stop any existing player.
	 *
	 * @since 2025/12/27
	 */
	public void stop()
	{
		synchronized (this)
		{
			// Stop playback
			Player player = this._player;
			if (player != null)
				try
				{
					player.stop();
				}
				catch (MediaException __e)
				{
					__e.printStackTrace();
				}
		}
	}
}
