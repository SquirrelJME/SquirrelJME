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
import cc.squirreljme.runtime.cldc.util.EnumerationToIterator;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.Iterator;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

/**
 * Basic browser for files.
 *
 * @since 2025/12/26
 */
public class BasicBrowser
	extends List
{
	/** Exit the application. */
	public static final Command EXIT =
		new Command("Exit", Command.EXIT, 1);
	
	/** Folder icon. */
	public static final Image ICON_FOLDER =
		Utils.tangoIcon("folder");
	
	/** Up icon. */
	public static final Image ICON_UP =
		Utils.tangoIcon("go-up");
	
	/** Audio file. */
	public static final Image ICON_AUDIO =
		Utils.tangoIcon("audio-x-generic");
	
	/** Image file. */
	public static final Image ICON_IMAGE =
		Utils.tangoIcon("image-x-generic");
	
	/** Video file. */
	public static final Image ICON_VIDEO =
		Utils.tangoIcon("video-x-generic");
	
	/** Other file. */
	public static final Image ICON_OTHER =
		Utils.tangoIcon("document");
	
	/** The binder this is attached to. */
	private final Reference<Binder> _binder;
	
	/**
	 * Initializes the browser. 
	 *
	 * @param __binder The binder this is attached to.
	 * @since 2025/12/26
	 */
	public BasicBrowser(Reference<Binder> __binder)
		throws NullPointerException
	{
		super("Select File", List.IMPLICIT);
		
		if (__binder == null)
			throw new NullPointerException("NARG");
		
		// Remember the binder we are using
		this._binder = __binder;
		
		// Add normal selection handling
		this.addCommand(List.SELECT_COMMAND);
		this.setSelectCommand(List.SELECT_COMMAND);
		
		// Normal exit
		this.addCommand(BasicBrowser.EXIT);
		
		// The binder listens for selections
		this.setCommandListener(__binder.get());
	}
	
	/**
	 * Browses the given file connection directory.
	 *
	 * @param __file The file to browse.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/26
	 */
	@SuppressWarnings("unchecked")
	public BasicBrowser browse(FileConnection __file)
		throws IOException, NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		// Clear the entire list
		this.deleteAll();
		
		// Always add the ability to go back up
		this.append("..", BasicBrowser.ICON_UP);
		
		try
		{
			// List contents of the directory and add them accordingly
			Iterator<String> it = new EnumerationToIterator<>(__file.list());
			while (it.hasNext())
			{
				// Get the file
				String fn = it.next();
				
				// Is a directory?
				Image icon;
				if (fn.endsWith("/"))
					icon = BasicBrowser.ICON_FOLDER;
				else
				{
					// Guess the MIME type based on the extension
					String mimeType = ContentTypeUtil.guessByPath(fn);
					if (mimeType == null)
						icon = BasicBrowser.ICON_OTHER;
					else if (ContentTypeUtil.isMediaAudio(mimeType))
						icon = BasicBrowser.ICON_AUDIO;
					else if (ContentTypeUtil.isMediaImage(mimeType))
						icon = BasicBrowser.ICON_IMAGE;
					else if (ContentTypeUtil.isMediaVideo(mimeType))
						icon = BasicBrowser.ICON_VIDEO;
					else
						icon = BasicBrowser.ICON_OTHER;
				}
				
				// Add to self, with the icon of the type of file we think 
				// this is
				this.append(fn, icon);
			}
		}
		
		// Failed directory read
		catch (IOException __e)
		{
			// Clear the list as it is not valid
			this.deleteAll();
			
			// But do add the ability to go back
			this.append("..", BasicBrowser.ICON_UP);
			
			// Retoss
			throw __e;
		}
		
		// Self
		return this;
	}
}
