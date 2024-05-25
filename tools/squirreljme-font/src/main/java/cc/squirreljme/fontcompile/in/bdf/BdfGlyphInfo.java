// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in.bdf;

import cc.squirreljme.fontcompile.in.GlyphInfo;
import cc.squirreljme.fontcompile.util.LineTokenizer;
import java.io.IOException;

/**
 * Represents a BDF glyph.
 *
 * @since 2024/05/18
 */
public class BdfGlyphInfo
	extends GlyphInfo
{
	/**
	 * Parses the BDF glyph information.
	 *
	 * @param __tokens The tokens for the {@code STARTCHAR}.
	 * @param __tokenizer The tokenizer to initialize from.
	 * @return The resultant glyph.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public static BdfGlyphInfo parse(String[] __tokens,
		LineTokenizer __tokenizer)
		throws IOException, NullPointerException
	{
		if (__tokens == null || __tokenizer == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
