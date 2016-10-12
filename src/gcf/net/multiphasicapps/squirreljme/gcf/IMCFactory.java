// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

/**
 * This class is used to create instances of inter-midlet connections.
 *
 * @since 2016/10/12
 */
public class IMCFactory
{
	/**
	 * Opens an inter-midlet connection, either as a client or as a server.
	 *
	 * @param __par The non-URI part.
	 * @param __timeouts Are timeouts used?
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public static Connection open(String __par, boolean __timeouts)
		throws IOException, NullPointerException
	{
		// Check
		if (__par == null || __timeouts == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EC05 The IMC URI must start with a double
		// slash. (The URI part)}
		if (!__par.startsWith("//"))
			throw new IllegalArgumentException(
				String.format("EC05 %s", __par));
		__par = __par.substring(2);
		
		// {@squirreljme.error EC06 Expected a semi-colon in the IMC URI.
		// (The URI part)}
		int sc = __par.indexOf(';');
		if (sc < 0)
			throw new IllegalArgumentException(
				String.format("EC06 %s", __par));
		String fullhost = __par.substring(0, sc);
		String flags = __part.substring(sc + 1);
		
		// Count the number of colons, there will be 2 (server/wild) or 4
		int[] colpos = new colpos[4];
		int cat = 0, numcols = 0;
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			// No more colons?
			int cp = fullhost.indexOf(':', cat);
			if (cp < 0)
				break;
			
			// {@squirreljme.error EC08 Too many colons in IMC host.
			// (The URI part)}
			if ((numcols + 1) > 4)
				throw new IllegalArgumentException(String.format(
					"EC08 %s", __par));
		}
		
		// {@squirreljme.error EC07 The target IMC host may only have 2 or
		// 4 colons. (The URI part; The number of colons)}
		if (numcols != 2 && numcols != 4)
			throw new IllegalArgumentException(String.format(
				"EC07 %s %d", __par, numcols));
		
		// Client connection to any midlet
		if (__par.startsWith("*"))
			throw new Error("TODO");
		
		// Client connection to a specific midlet
		else if (!__par.startsWith(":"))
			throw new Error("TODO");
		
		// Server connection
		else
			throw new Error("TODO");
	}
}

