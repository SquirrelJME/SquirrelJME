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
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import org.intellij.lang.annotations.Language;

/**
 * Binder between the browser and media player.
 *
 * @since 2025/12/27
 */
public final class Binder
	implements CommandListener
{
	/** Reference to self. */
	private final Reference<Binder> _self =
		new WeakReference<>(this);
	
	/** The browser. */
	final BasicBrowser _browser =
		new BasicBrowser(this._self);
	
	/** The media player. */
	final MediaPlayer _player =
		new MediaPlayer(this._self);
	
	/** The display to use. */
	final Display _display;
	
	/** The universal file connection. */
	final FileConnection connection;
	
	/**
	 * Initializes the binder.
	 *
	 * @param __display The display to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/27
	 */
	public Binder(Display __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this._display = __display;
		
		// Open the only sole file connection at the all volumes root which
		// is specific to SquirrelJME
		try
		{
			// We do have to start somewhere
			this.connection = (FileConnection)Connector.open(
				"file://!%3Fx-squirreljme-all-volumes%3A%2F%2F%3F!/",
				Connector.READ);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e.getMessage(), __e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public void commandAction(Command __command, Displayable __displayable)
	{
		FileConnection connection = this.connection;
		BasicBrowser browser = this._browser;
		MediaPlayer player = this._player;
		
		// Change directory or view/play a media
		if (__command == List.SELECT_COMMAND)
		{
			// Browse to the specified file
			int dx = browser.getSelectedIndex();
			if (dx >= 0)
				try
				{
					// Try setting this
					connection.setFileConnection(browser.getString(dx));
					
					// Refresh the display since our location changed
					this.refresh();
				}
				catch (IOException __e)
				{
					__e.printStackTrace();
				}
		}
		
		// Exit the media player
		else if (__command == BasicBrowser.EXIT)
		{
			System.exit(0);
		}
	}
	
	/**
	 * Refreshes the current display, either browsing the current file if it
	 * is a directory or playing/viewing its media.
	 *
	 * @throws IOException On read errors.
	 * @since 2025/12/30
	 */
	public void refresh()
		throws IOException
	{
		FileConnection connection = this.connection;
		BasicBrowser browser = this._browser;
		MediaPlayer player = this._player;
		
		// If browsing a directory, browse the contents
		Displayable show;
		if (connection.isDirectory())
			show = browser.browse(connection);
			
		// Otherwise view the content
		else if (connection instanceof InputConnection)
			show = player.play(connection);
			
		// Is some other kind of connection we cannot play?
		else
			throw new IOException(connection.getURL());
		
		// Show on the display!
		this._display.setCurrent(show);
	}
}
