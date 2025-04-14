// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.io.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * File utilities.
 *
 * @since 2024/06/08
 */
public final class FileUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2024/06/08
	 */
	private FileUtils()
	{
	}
	
	/**
	 * Lists the contents of the given directory.
	 *
	 * @param __path The path to list.
	 * @return The list of files.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/08
	 */
	public static List<Path> listFiles(Path __path)
		throws IOException, NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__path))
		{
			for (Path path : ds)
				result.add(path);
		}
		
		return result;
	}
}
