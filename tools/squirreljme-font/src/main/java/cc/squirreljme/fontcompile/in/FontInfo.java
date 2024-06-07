// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.in;

import cc.squirreljme.fontcompile.in.bdf.BdfGlyphInfo;
import cc.squirreljme.fontcompile.util.GlyphId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Iterator;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for font information.
 *
 * @since 2024/05/17
 */
public abstract class FontInfo
	implements Iterable<GlyphInfo>
{
	/** The name of this font. */
	public final String name;
	
	/** The invalid codepoint. */
	public final GlyphId invalidCodepoint;
	
	/** The font ascent. */
	public final int ascent;
	
	/** The font descent. */
	public final int descent;
	
	/** Bounding box width. */
	public final int bbw;
	
	/** Bounding box height. */
	public final int bbh;
	
	/** X offset. */
	public final int bbx;
	
	/** Y offset. */
	public final int bby;
	
	/** The pixel size of the font. */
	public final int pixelSize;
	
	/** The glyphs in the font. */
	protected final Map<GlyphId, GlyphInfo> glyphs;
	
	/**
	 * Initializes the base font info.
	 *
	 * @param __name The name of the font.
	 * @param __glyphs The glyphs to use.
	 * @param __invalidCodepoint The invalid glyph ID.
	 * @param __pixelSize The pixel size of the font.
	 * @param __bbw The bounding box width.
	 * @param __bbh The bounding box height.
	 * @param __bbx The X offset.
	 * @param __bby The Y offset.
	 * @param __ascent The font ascent.
	 * @param __descent The font descent.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	protected FontInfo(String __name,
		Map<GlyphId, ? extends GlyphInfo> __glyphs,
		GlyphId __invalidCodepoint,
		int __pixelSize, int __bbw, int __bbh,
		int __bbx, int __bby, int __ascent, int __descent)
		throws NullPointerException
	{
		if (__glyphs == null || __invalidCodepoint == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.glyphs = UnmodifiableMap.of(new SortedTreeMap<>(__glyphs));
		this.invalidCodepoint = __invalidCodepoint;
		this.pixelSize = __pixelSize;
		this.bbw = __bbw;
		this.bbh = __bbh;
		this.bbx = __bbx;
		this.bby = __bby;
		this.ascent = __ascent;
		this.descent = __descent;
	}
	
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
	 * Returns the glyph with the given codepoint.
	 *
	 * @param __codepoint The codepoint to use.
	 * @return The glyph for the given codepoint.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/26
	 */
	public GlyphInfo byCodepoint(GlyphId __codepoint)
		throws NullPointerException
	{
		if (__codepoint == null)
			throw new NullPointerException("NARG");
		
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
		return this.byCodepoint(this.invalidCodepoint);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/24
	 */
	@Override
	public final Iterator<GlyphInfo> iterator()
	{
		return this.glyphs.values().iterator();
	}
	
	/**
	 * Returns the number of glyphs.
	 *
	 * @return The glyph count.
	 * @since 2024/06/03
	 */
	public int size()
	{
		return this.glyphs.size();
	}
}
