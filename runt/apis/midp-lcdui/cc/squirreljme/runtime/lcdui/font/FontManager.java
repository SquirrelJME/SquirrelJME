// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

import cc.squirreljme.runtime.cldc.service.ServiceAccessor;
import cc.squirreljme.runtime.lcdui.DisplayManager;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import javax.microedition.lcdui.Font;
import net.multiphasicapps.collections.SortedTreeMap;

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
	
	/** The families which are available. */
	private final Map<FontFamilyName, FontFamily> _families =
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
		FontManager fm = ServiceAccessor.<FontManager>service(
			FontManager.class);
		FONT_MANAGER = (fm == null ? new DefaultFontManager() : fm);
	}
	
	/**
	 * Returns the alias for the given family name so that it always refers
	 * to another family name.
	 *
	 * @param __n The name of the font to alias.
	 * @return The alias for that given name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public abstract FontFamilyName aliasFamilyName(FontFamilyName __n)
		throws NullPointerException;
	
	/**
	 * Loads the given font family.
	 *
	 * @param __n The font family to load.
	 * @return The family for the given font.
	 * @throws IllegalArgumentException If the font does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 217/10/20
	 */
	protected abstract FontFamily loadFamily(FontFamilyName __n)
		throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Returns the family to use for the given face.
	 *
	 * @param __face The face to create.
	 * @return The family of the font to use for the face.
	 * @since 2017/10/20
	 */
	public final FontFamily createFamily(int __face)
	{
		switch (__face)
		{
				// Monospaced
			case Font.FACE_MONOSPACE:
				return createFamily("Monospaced");
				
				// Proportional
			case Font.FACE_PROPORTIONAL:
				return createFamily("SansSerif");
			
				// Default font
			case Font.FACE_SYSTEM:
			default:
				return createFamily("Default");
		}
	}
	
	/**
	 * Returns the family to use for the given font name.
	 *
	 * @param __name The name of the font to get.
	 * @return The family for the given font.
	 * @throws IllegalArgumentException If the given family name does not
	 * exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public final FontFamily createFamily(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Setup font family name
		FontFamilyName fn = new FontFamilyName(__name),
			alias = aliasFamilyName(fn);
		if (alias != null)
			fn = alias;
		
		// Families are only initialized once
		Map<FontFamilyName, FontFamily> families = this._families;
		synchronized (families)
		{
			// Use pre-existing family
			FontFamily rv = families.get(fn);
			if (rv != null)
				return rv;
			
			// {@squirreljme.error EB08 Could not load the specified font
			// family because it does not exist. (The name of the family)}
			rv = loadFamily(fn);
			if (rv == null)
				throw new IllegalArgumentException(String.format("EB08 %s",
					fn));
			
			// Debug
			System.err.printf("DEBUG -- Created font family: %s%n", fn);
			
			// Cache for later
			families.put(fn, rv);
			return rv;
		}
	}
	
	/**
	 * Creates a font with the specified parameters.
	 *
	 * @param __face The font face, this is a single value.
	 * @param __style The style of the font, this may be a combination of
	 * values.
	 * @param __size The size of the font in pixels.
	 * @return The nearest font which matches the specified parameters.
	 * @throws IllegalArgumentException If the input parameters are not valid.
	 * @since 2017/05/25
	 */
	public final FontHandle createFont(int __face, int __style, int __size)
		throws IllegalArgumentException
	{
		return createFont(createFamily(__face), __style, __size);
	}
	
	/**
	 * Creates a font with the specified parameters.
	 *
	 * @param __fam The family of the font.
	 * @param __style The style of the font, this may be a combination of
	 * values.
	 * @param __size The size of the font in pixels.
	 * @return The nearest font which matches the specified parameters.
	 * @throws IllegalArgumentException If the input parameters are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/25
	 */
	public final FontHandle createFont(FontFamily __fam, int __style,
		int __size)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fam == null)
			throw new NullPointerException("NARG");
		
		// Needs to be thread safe
		Map<Integer, FontHandle> handles = this._handles;
		synchronized (handles)
		{
			// Go through fonts to find a compatible font which already exists
			for (FontHandle h : handles.values())
				if (h.isCompatible(__fam, __style, __size))
					return h;
			
			// Create new handle
			int rid = ++this._nexthandle;
			FontHandle rv = new FontHandle(rid, __fam, __style, __size);
			handles.put(rid, rv);
			return rv;
		}
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
			// {@squirreljme.error EB09 Attempt to get the handle for a font
			// which does not have a valid handle mapping.}
			FontHandle rv = handles.get(__f.hashCode());
			if (rv == null)
				throw new RuntimeException("EB09");
			return rv;
		}
	}
}

