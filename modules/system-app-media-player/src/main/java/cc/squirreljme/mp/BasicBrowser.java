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
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.List;

/**
 * Basic browser for files.
 *
 * @since 2025/12/26
 */
public class BasicBrowser
	extends List
{
	/**
	 * Initializes the browser. 
	 *
	 * @since 2025/12/26
	 */
	public BasicBrowser()
	{
		super("Select File", List.EXCLUSIVE);
	}
	
	/**
	 * Browses the given file connection directory.
	 *
	 * @param __file The file to browse.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/26
	 */
	public void browse(FileConnection __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
