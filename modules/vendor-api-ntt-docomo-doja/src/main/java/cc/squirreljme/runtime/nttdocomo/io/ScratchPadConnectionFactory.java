// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.ConnectionOption;

/**
 * Factory for creating {@link ScratchPadConnection}.
 *
 * These are in the format of {@code scratchpad:///0;pos=0,length=0}.
 *
 * @see ScratchPadConnection
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public class ScratchPadConnectionFactory
	implements CustomConnectionFactory
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public Connection connect(String __part, int __mode, boolean __timeouts,
		ConnectionOption<?>[] __opts)
		throws IOException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AH05 Application JAD never set any available
		// scratch pads.}
		ScratchPadParams params = ScratchPadParams.__load();
		if (params == null)
			throw new ConnectionNotFoundException("AH05");
		
		// {@squirreljme.error AH0a URI is missing the starting triple
		// slashes. (The URI part)}
		if (!__part.startsWith("///"))
			throw new ConnectionNotFoundException("AH0a " + __part);
		
		// Which scratchpad is wanted?
		int wantPad;
		int wantPos = 0;
		int wantLen = Integer.MIN_VALUE;
		try
		{
			// No parameters to the URI?
			int semi = __part.indexOf(';');
			if (semi < 0)
				wantPad = Integer.parseInt(__part.substring(3),
					10);
				
				// Parameters are given
			else
			{
				// The wanted scratch pad
				wantPad = Integer.parseInt(__part.substring(3, semi), 10);
				
				// Handle various parameters
				String parms = __part.substring(semi + 1);
				if (!parms.isEmpty())
					for (String item : StringUtils.basicSplit(',',
						parms))
					{
						// {@squirreljme.error AH0d Missing equal sign in the
						// parameter. (The URI part)}
						int eq = item.indexOf('=');
						if (eq < 0)
							throw new ConnectionNotFoundException(
								"AH0d " + __part);
						
						// Parse the value
						int val = Integer.parseInt(item.substring(
							eq + 1), 10);
						
						// And store it accordingly
						switch (item.substring(0, eq))
						{
							case "pos":
								// {@squirreljme.error AH0f Invalid position
								// for scratchpad. (The URI part)}
								if (val < 0)
									throw new ConnectionNotFoundException(
										"AH0f " + __part);
								wantPos = val;
								break;
							
							case "length":
								// {@squirreljme.error AH0g Invalid length
								// for scratchpad. (The URI part)}
								if (val <= 0)
									throw new ConnectionNotFoundException(
										"AH0g " + __part);
								wantLen = val;
								break;
							
							// {@squirreljme.error AH0e Invalid parameter
							// for scratch pads. (The URI part)}
							default:
								throw new ConnectionNotFoundException(
									"AH0e " + __part);
						}
					}
			}
		}
		
		// {@squirreljme.error AH0b Invalid number value in URI. (The URI
		// part)}
		catch (NumberFormatException __e)
		{
			throw new ConnectionNotFoundException("AH0b " + __part);
		}
		
		// {@squirreljme.error AH0c Requested scratch pad is out of bounds.
		// (The scratch pad part)}
		if (wantPad < 0 || wantPad >= params.count())
			throw new ConnectionNotFoundException("AH0c " + __part);
		
		// Determine default max length
		if (wantLen < 0)
			wantLen = params.getLength(wantPad) - wantPos;
		
		// {@squirreljme.error AH0h Requested position and length exceeds
		// the bound of the scratch pad. (The URI part; The desired position;
		// The desired length; The max length)}
		if (wantPos + wantLen > params.getLength(wantPad))
			throw new ConnectionNotFoundException(
				String.format("AH0h %s %d %d %d", __part, wantPos, wantLen,
					params.getLength(wantPad)));
		
		// Initialize the connection
		return new ScratchPadConnection(params, wantPad, wantPos, wantLen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/30
	 */
	@Override
	@SquirrelJMEVendorApi
	public String scheme()
	{
		return "scratchpad";
	}
}
