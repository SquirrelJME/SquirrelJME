// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class goes through all projects and reorders the error codes so that
 * they appear in order. This program should really only be ran when error
 * codes are a complete mess after a refactor and they need to cleaned up.
 *
 * @since 2017/10/26
 */
public class ReorderErrors
{
	/** The path to reorder errors for. */
	protected final Path path;
	
	/**
	 * Initializes the error reordering on the given path.
	 *
	 * @param __p The path to reorder.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/26
	 */
	public ReorderErrors(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
	}
	
	/**
	 * Processes everything.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2017/10/26
	 */
	public void run()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/10/26
	 */
	public static void main(String... __args)
	{
		if (__args == null)
			__args = new String[0];
		
		for (String a : __args)
			try
			{
				new ReorderErrors(Paths.get(a)).run();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	}
}

