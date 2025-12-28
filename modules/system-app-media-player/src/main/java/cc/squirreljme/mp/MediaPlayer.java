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
			
			throw Debugging.todo();
		}
	}
	
	@Override
	protected void keyPressed(int __code)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void keyRepeated(int __code)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/27
	 */
	@Override
	protected void paint(Graphics __g)
	{
		throw Debugging.todo();
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
				
			if (true)
				throw Debugging.todo();
		}
		
		// Self
		return this;
	}
	
	@Override
	protected void pointerDragged(int __x, int __y)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void pointerPressed(int __x, int __y)
	{
		throw Debugging.todo();
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
			throw Debugging.todo();
		}
	}
}
