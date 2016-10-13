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

import java.io.IOException;
import javax.microedition.io.Connection;
import net.multiphasicapps.squirreljme.midletid.MidletSuiteID;
import net.multiphasicapps.squirreljme.midletid.MidletVersion;

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
	 * @throws IllegalArgumentException If the URI is incorrect.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public static Connection open(String __par, boolean __timeouts)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__par == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EC05 IMC connections must start with two
		// forward slashes. (The scheme specific part)}
		if (!__par.startsWith("//"))
			throw new IllegalArgumentException(String.format("EC05", __par));
		
		// {@squirreljme.error EC06 IMC connections must have a semi-colon
		// before the flags, even if the flags are not specified. (The scheme
		// specific part)}
		int semi = __par.indexOf(';');
		if (semi < 0)
			throw new IllegalArgumentException(String.format("EC06", __par));
		
		// Split host and options
		String rest = __par.substring(2, semi);
		String flags = __par.substring(semi + 1);
		
		// Host server?
		boolean isclient;
		MidletSuiteID connect;
		if (rest.startsWith(":"))
		{
			isclient = false;
			connect = null;
			
			// Trim
			rest = rest.substring(1);
		}
		
		// Client connection
		else
		{
			isclient = true;
			
			// Connect to any midlet available?
			if (rest.startsWith("*:"))
			{
				connect = null;
				
				// Trim
				rest = rest.substring(2);
			}
			
			// Connect to a specific midlet
			else
			{
				// {@squirreljme.error EC09 Expected to find a colon in
				// the IMC midlet target. (The scheme specific part)}
				int cola = rest.indexOf(':');
				if (cola < 0)
					throw new IllegalArgumentException(String.format(
						"EC09 %s", __par));
				
				// {@squirreljme.error EC0a Expected to find a second colon
				// in the IMC midlet target. (The scheme specific part)}
				int colb = rest.indexOf(':', cola + 1);
				if (colb < 0)
					throw new IllegalArgumentException(String.format(
						"EC0a %s", __par));
				
				// {@squirreljme.error EC0a Expected to find a third colon
				// in the IMC midlet target. (The scheme specific part)}
				int colc = rest.indexOf(':', colb + 1);
				if (colb < 0)
					throw new IllegalArgumentException(String.format(
						"EC0b %s", __par));
				
				// Parse suite ID
				connect = new MidletSuiteID(rest.substring(0, colc), true);
				
				// The rest is anything after the third
				rest = rest.substring(colc + 1);
			}
		}
		
		// {@squirreljme.error EC07 IMC connection does not have a colon
		// to separate the server name and the version. (The scheme specific
		// part)}
		int col = rest.indexOf(':');
		if (col < 0)
			throw new IllegalArgumentException(
				String.format("EC07 %s", __par));
		
		// Decode server name and version
		String name = rest.substring(0, col);
		MidletVersion version = new MidletVersion(rest.substring(col + 1));
		
		// Parse authorization mode
		boolean authmode;
		if (flags.length() > 0)
			if (flags.equals("authmode=true"))
				authmode = true;
			else if (flags.equals("authmode=false"))
				authmode = false;
			
			// {@squirreljme.error EC08 Unknown flags specified in IMC
			// connection. (The scheme specific part)}
			else
				throw new IllegalArgumentException(String.format("EC08 %s",
					__par));
		else
			authmode = false;
		
		// Create client socket
		if (isclient)
			return new IMCClient(connect, name, version, authmode);
		
		// Create server socket
		else
			return new IMCServer(name, version, authmode);
	}
}

