// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in.sfdir;

import cc.squirreljme.fontcompile.in.FontInfo;
import cc.squirreljme.fontcompile.in.bdf.BdfFontInfo;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Represents a {@code .sfdir} font.
 *
 * @since 2024/05/18
 */
public class SfdFontInfo
	extends FontInfo
{
	/**
	 * Parses the given font.
	 *
	 * @param __in The input path.
	 * @return The parsed font.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/19
	 */
	public static SfdFontInfo parse(Path __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
