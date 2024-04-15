// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

/**
 * Developer note session storage.
 *
 * @since 2020/06/26
 */
final class __DeveloperNoteSession__
{
	/** The file path. */
	public final String filePath;
	
	/** The number of times this was saved. */
	int _saveCount;
	
	/** Submitted content data. */
	byte[] _content =
		new byte[0];
	
	/**
	 * Initializes the session details.
	 * 
	 * @param __filePath The file path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public __DeveloperNoteSession__(String __filePath)
		throws NullPointerException
	{
		if (__filePath == null)
			throw new NullPointerException("NARG");
		
		this.filePath = __filePath;
	}
}
