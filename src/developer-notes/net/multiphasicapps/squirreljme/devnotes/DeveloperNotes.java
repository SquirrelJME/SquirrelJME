// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.devnotes;

import java.io.InputStream;

/**
 * This is used so that the resources in the JAR may be obtained from outside
 * of this JAR.
 *
 * @since 2016/06/16
 */
public final class DeveloperNotes
{
	/**
	 * Not used.
	 *
	 * @since 2016/06/16
	 */
	private DeveloperNotes()
	{
	}
	
	/**
	 * Returns the stream which is associated with the given develop on the
	 * given date.
	 *
	 * @param __name The developer name.
	 * @param __y The year.
	 * @param __m The month.
	 * @param __d The day.
	 * @return The stream of the developer notes or {@code null} if there is
	 * no such resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public static InputStream DeveloperNotes(String __name, int __y, int __m,
		int __d)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Get it
		return DeveloperNotes.class.getResourceAsStream(String.format(
			"/%s/%04d/%02s/%02d.mkd", __name, __y, __m, __d));
	}
}

