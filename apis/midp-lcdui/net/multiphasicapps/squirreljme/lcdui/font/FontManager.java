// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.font;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.microedition.lcdui.Font;
import net.multiphasicapps.squirreljme.unsafe.SystemEnvironment;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used as a service and provides management for fonts to be
 * provided to the LCDUI interface.
 *
 * The font manager for the most part just provides some basic details along
 * with their graphical glyphs.
 *
 * @since 2017/10/01
 */
public abstract class FontManager
{
	/** The single instance font manager to use. */
	public static final FontManager FONT_MANAGER;
	
	/** The handles of every font which has been created. */
	private final Map<Integer, FontHandle> _handles =
		new SortedTreeMap<>();
	
	/** The ID of the next font to create. */
	private volatile int _nexthandle;
	
	/**
	 * Locates the font manager to use in this instance or returns the
	 * default font manager with a set of default provided fonts.
	 *
	 * @since 2017/10/20
	 */
	static
	{
		// Use either the provided font manager or a default if none was set
		FontManager fm = SystemEnvironment.<FontManager>systemService(
			FontManager.class);
		FONT_MANAGER = (fm == null ? new DefaultFontManager() : fm);
	}
	
	/**
	 * Creates a font with the specified parameters.
	 *
	 * @param __face The font face, this is a single value.
	 * @param __style The style of the font, this may be a combination of
	 * values.
	 * @param __size The size of the font, this is a single value.
	 * @return The nearest font which matches the specified parameters.
	 * @throws IllegalArgumentException If the input parameters are not valid.
	 * @since 2017/05/25
	 */
	public final FontHandle createFont(int __face, int __style, int __size)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the handle for the given font or returns {@code null} if no
	 * font uses that handle.
	 *
	 * @return The handle for the given font.
	 */
	public final FontHandle getHandle(Font __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Needs to be thread safe
		Map<Integer, FontHandle> handles = this._handles;
		synchronized (handles)
		{
			// {@squirreljme.error EB1q Attempt to get the handle for a font
			// which does not have a valid handle mapping.}
			FontHandle rv = handles.get(__f.hashCode());
			if (rv == null)
				throw new RuntimeException("EB1q");
			return rv;
		}
	}
}

