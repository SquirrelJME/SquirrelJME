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

import javax.microedition.lcdui.Font;

/**
 * This is a font manager which acts as a provider for a default set of fonts.
 * The default fonts are quite primitive and are mostly intended to be used
 * as a fallback when no native fonts are provided.
 *
 * @since 2017/10/20
 */
@Deprecated
public class DefaultFontManager
	extends FontManager
{
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public FontFamilyName aliasFamilyName(FontFamilyName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		if (__n.equals(FontFamilyName.DEFAULT) ||
			__n.equals(FontFamilyName.DIALOG) ||
			__n.equals(FontFamilyName.DIALOG_INPUT))
			return FontFamilyName.SANS_SERIF;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	protected FontFamily loadFamily(FontFamilyName __n)
		throws IllegalArgumentException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return new DefaultFontFamily(__n);
	}
}

