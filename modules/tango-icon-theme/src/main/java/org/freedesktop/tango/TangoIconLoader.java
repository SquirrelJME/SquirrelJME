// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package org.freedesktop.tango;

import java.io.InputStream;

/**
 * Used to load Tango icons.
 *
 * @since 2022/10/03
 */
public final class TangoIconLoader
{
	/**
	 * Not used.
	 * 
	 * @since 2022/10/03
	 */
	private TangoIconLoader()
	{
	}
	
	/**
	 * Loads a given icon.
	 * 
	 * @param __size The size of the icon to load.
	 * @param __icon The icon to load.
	 * @return The stream to the loaded icon.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/03
	 */
	public static InputStream loadIcon(int __size, String __icon)
		throws NullPointerException
	{
		if (__icon == null)
			throw new NullPointerException("NARG");
		
		return TangoIconLoader.class.getResourceAsStream(
			String.format("%d/%s.png", __size, __icon));
	}
}
