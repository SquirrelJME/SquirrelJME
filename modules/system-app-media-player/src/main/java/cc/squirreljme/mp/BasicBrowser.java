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
import java.lang.ref.Reference;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.List;

/**
 * Basic browser for files.
 *
 * @since 2025/12/26
 */
public class BasicBrowser
	extends List
{
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
		super("Select File", List.EXCLUSIVE);
		
		if (__binder == null)
			throw new NullPointerException("NARG");
		
		this._binder = __binder;
	}
	
	/**
	 * Browses the given file connection directory.
	 *
	 * @param __file The file to browse.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/26
	 */
	public BasicBrowser browse(FileConnection __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
