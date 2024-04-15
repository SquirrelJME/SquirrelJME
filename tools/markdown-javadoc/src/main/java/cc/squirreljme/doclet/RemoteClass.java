// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

/**
 * Represents a class that is in a remote project.
 *
 * @since 2022/08/29
 */
public final class RemoteClass
{
	/** The path of the file locally in the project. */
	public final String markdownPath;
	
	/** The name of the project this is in. */
	public final String projectName;
	
	/** The name of this class. */
	public final String className;
	
	/**
	 * Initializes the remote class.
	 * 
	 * @param __projectName The project name.
	 * @param __markdownPath The path to the markdown file.
	 * @param __className The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/29
	 */
	public RemoteClass(String __projectName, String __markdownPath,
		String __className)
		throws NullPointerException
	{
		if (__projectName == null || __markdownPath == null ||
			__className == null)
			throw new NullPointerException("NARG");
		
		this.projectName = __projectName;
		this.markdownPath = __markdownPath;
		this.className = __className;
	}
}
