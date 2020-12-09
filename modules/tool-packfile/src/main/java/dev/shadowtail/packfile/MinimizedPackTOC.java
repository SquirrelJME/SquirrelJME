// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.packfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Minimized pack file table of contents.
 *
 * @since 2020/12/09
 */
public final class MinimizedPackTOC
{
	/**
	 * Decodes the pack file table of contents.
	 * 
	 * @param __in The stream to read from.
	 * @return The read table of contents.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/09
	 */
	public static MinimizedPackTOC decode(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
