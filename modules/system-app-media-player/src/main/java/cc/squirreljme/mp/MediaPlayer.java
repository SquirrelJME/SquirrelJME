// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.util.Objects;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
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
	
	/** The last known media time. */
	private volatile long _lastMediaTime;
	
	/** The content path. */
	private volatile String contentPath;
	
	/** The content type. */
	private volatile String contentType;
	
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
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected void keyPressed(int __code)
	{
		Player player = this._player;
		
		// Store and get the last known media time
		long lastMediaTime = this._lastMediaTime;
		if (player != null && player.getState() == Player.STARTED)
		{
			lastMediaTime = player.getMediaTime();
			this._lastMediaTime = lastMediaTime;
		}
		
		// Cap media time to zero
		if (lastMediaTime < 0)
			lastMediaTime = 0;
		
		// Which action happened?
		switch (this.getGameAction(__code))
		{
				// Play/Stop
			case Canvas.GAME_A:
				if (player != null)
					try
					{
						// Stop playing?
						if (player.getState() >= Player.STARTED)
							player.stop();
						
						// Playback starting at the correct media time
						else
						{
							player.setMediaTime(lastMediaTime);
							player.start();
						}
					}
					catch (MediaException __e)
					{
						__e.printStackTrace();
					}
				break;
				
				// Browse Files
			case Canvas.GAME_B:
				// Force stop the player
				if (player != null)
					try
					{
						// Close the player
						player.close();
						
						// Remove reference to this
						this._player = null;
					}
					catch (RuntimeException __e)
					{
						__e.printStackTrace();
					}
				
				// Restore the file browser
				Binder binder = this._binder.get();
				if (binder != null)
					try
					{
						// Go to the parent directory
						binder.connection.setFileConnection("..");
						
						// Refresh the display
						binder.refresh();
					}
					catch (IOException __e)
					{
						__e.printStackTrace();
					}
				break;
				
				// Never loop
			case Canvas.GAME_C:
				if (player != null)
					try
					{
						// Can only set the count if stopped
						boolean play = (player.getState() >= Player.STARTED);
						if (play)
							player.stop();
						
						// Set the loop count
						player.setLoopCount(1);
						
						// Resume playing if it was stopped
						if (play)
							player.start();
					}
					catch (RuntimeException|MediaException __e)
					{
						__e.printStackTrace();
					}
				break;
				
				// Loop forever
			case Canvas.GAME_D:
				if (player != null)
					try
					{
						// Can only set the count if stopped
						boolean play = (player.getState() >= Player.STARTED);
						if (play)
							player.stop();
						
						// Set the loop count
						player.setLoopCount(-1);
						
						// Resume playing if it was stopped
						if (play)
							player.start();
					}
					catch (RuntimeException|MediaException __e)
					{
						__e.printStackTrace();
					}
				break;
				
				// Seek -5
			case Canvas.LEFT:
				lastMediaTime -= 5_000_000;
				if (lastMediaTime < 0)
					lastMediaTime = 0;
				
				// Set the time if the player is valid
				if (player != null)
					try
					{
						player.setMediaTime(lastMediaTime);
						this._lastMediaTime = lastMediaTime;
					}
					catch (MediaException __e)
					{
						__e.printStackTrace();
					}
				break;
				
				// Seek +5
			case Canvas.RIGHT:
				lastMediaTime += 5_000_000;
				
				// Set the time if the player is valid
				if (player != null)
					try
					{
						player.setMediaTime(lastMediaTime);
						this._lastMediaTime = lastMediaTime;
					}
					catch (MediaException __e)
					{
						__e.printStackTrace();
					}
				break;
		}
		
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
		int fh = (font != null ? font.getHeight() : 12);
		
		// How big is this canvas?
		int w = this.getWidth();
		int h = this.getHeight();
		
		// Get the address the media is at
		String url = null;
		Binder binder = this._binder.get();
		
		// Unknown URL? Just point to unknown
		url = this.contentPath;
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
		__g.drawString(this.contentType, ix, iy + fh, 0);
		
		// Draw instructions
		__g.drawString(String.format("[%s (A)] Play/Stop",
				this.getKeyName(this.getKeyCode(Canvas.GAME_A))),
			ix, iy + (fh * 3), 0);
		__g.drawString(String.format("[%s (B)] Browse Files",
				this.getKeyName(this.getKeyCode(Canvas.GAME_B))),
			ix, iy + (fh * 4), 0);
		__g.drawString(String.format("[%s (Left)] Seek -5s",
				this.getKeyName(this.getKeyCode(Canvas.LEFT))),
			ix, iy + (fh * 5), 0);
		__g.drawString(String.format("[%s (Right)] Seek +5s",
				this.getKeyName(this.getKeyCode(Canvas.RIGHT))),
			ix, iy + (fh * 6), 0);
		__g.drawString(String.format("[%s (C)] Do not loop",
				this.getKeyName(this.getKeyCode(Canvas.GAME_C))),
			ix, iy + (fh * 7), 0);
		__g.drawString(String.format("[%s (D)] Loop forever",
				this.getKeyName(this.getKeyCode(Canvas.GAME_D))),
			ix, iy + (fh * 8), 0);
		
		// Where are we at and how long is this media?
		long trk = (player != null ? player.getMediaTime() : 0);
		long dur = (player != null ? player.getDuration() : 0);
		
		// Store the media time for playing back
		if (trk > 0)
			this._lastMediaTime = trk;
		
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
		
		// Draw the media time
		__g.setColor(0x000000);
		this.paintTime(__g, trk,
			fx + bw, by - (fh * 2));
		this.paintTime(__g, dur,
			fx + bw, by - fh);
		
		// Print current state
		if (player != null)
		{
			// Name based state?
			String state = Utils.formatState(player.getState());
			
			// Draw the state
			__g.drawString(state, bx, by + bh + 2, 0);
		}
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
		
		// Draw the time
		__g.drawString(Utils.formatTime(__micros), __x, __y,
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
		InputStream in = null;
		Player player;
		try
		{
			// Open the input
			in = __conn.openInputStream();
			
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
			
			// Cache these
			if (__conn instanceof HttpConnection)
				this.contentPath = ((HttpConnection)__conn).getURL();
			else if (__conn instanceof FileConnection)
				this.contentPath = ((FileConnection)__conn).getPath();
			else
				this.contentPath = null;
			this.contentType = contentType;
			
			// Set the title
			this.setTitle(Objects.toString(this.contentPath,
				this.contentType));
		}
		
		// Could not load this media
		catch (IOException|MediaException __e)
		{
			// Print error
			__e.printStackTrace();
			
			// Failed, so close the input
			if (in != null)
				try
				{
					in.close();
				}
				catch (IOException __f)
				{
					__f.printStackTrace();
				}
			
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
