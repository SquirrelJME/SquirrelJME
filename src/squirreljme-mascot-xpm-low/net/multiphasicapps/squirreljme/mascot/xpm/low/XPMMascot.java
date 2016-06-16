// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mascot.xpm.low;

import java.io.InputStream;

/**
 * This is used as a base class to provide access.
 *
 * @since 2016/06/16
 */
public final class XPMMascot
{
	/**
	 * Not used.
	 *
	 * @since 2016/06/16
	 */
	private XPMMascot()
	{
	}
	
	/**
	 * Returns the input stream for the head icon of a given size.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The resource as an input stream or {@code null} if not found.
	 * @since 2016/06/16
	 */
	public static InputStream head(int __w, int __h)
	{
		return XPMMascot.class.getResourceAsStream("head_%dx%d.xpm", __w, __h);
	}
}

