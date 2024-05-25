// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for font information.
 *
 * @since 2024/05/17
 */
public abstract class FontInfo
	implements Iterable<GlyphInfo>
{
	/**
	 * Returns the glyph with the given codepoint.
	 *
	 * @param __codepoint The codepoint to use.
	 * @return The glyph for the given codepoint.
	 * @since 2024/05/24
	 */
	public GlyphInfo byCodepoint(int __codepoint)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the glyph to use for invalid characters.
	 *
	 * @return The glyph for invalid characters.
	 * @since 2024/05/24
	 */
	public GlyphInfo invalidCodepoint()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/24
	 */
	@Override
	public Iterator<GlyphInfo> iterator()
	{
		throw Debugging.todo();
	}
}
