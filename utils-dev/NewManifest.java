// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This translates from the old manifest format to the new manifest format.
 *
 * @since 2016/10/22
 */
public class NewManifest
{
	/** Compares two strings case insensitively and by numerical value. */
	public static Comparator<String> KEY_COMPARE =
		new Comparator<String>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/10/22
			 */
			@Override
			public int compare(String __a, String __b)
			{
				// Get length of both
				int la = __a.length(), lb = __b.length();
				int mm = Math.min(la, lb);
				
				// Compare each character
				for (int ia = 0, ib = 0; ia < mm && ib < mm;)
				{
					// Read both characters
					char ca = __a.charAt(ia++),
						cb = __a.charAt(ib++);
					
					// Are these digits?
					boolean diga = Character.isDigit(ca),
						digb = Character.isDigit(cb);
					
					// Numbers before letters
					if (diga && !digb)
						return -1;
					
					// Letters after numbers
					else if (!diga && digb)
						return 1;
					
					// Both letters
					else if (!diga && !digb)
					{
						int rv = Character.compare(Character.toLowerCase(ca),
							Character.toLowerCase(cb));
						if (rv != 0)
							return 0;
					}
					
					// Both digits
					else
					{
						throw new Error("TODO");
					}
				}
				
				// Shorter is lower
				if (la < lb)
					return -1;
				
				// Longer is higher
				else if (la > lb)
					return 1;
				
				// Otherwise the same
				return 0;
			}
		};
	
	/**
	 * Main entry point.
	 *
	 * @param __args Not used.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/22
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Read standard input
		try (InputStream is = System.in)
		{
			// Read manifest
			Manifest man = new Manifest(is);
			
			// Input and output maps
			NavigableMap<String, String> input = new TreeMap<>(KEY_COMPARE);
			NavigableMap<String, String> output = new TreeMap<>(KEY_COMPARE);
			
			// Go through the main attributes
			for (Map.Entry<Object, Object> e : man.getMainAttributes().
				entrySet())
			{
				// Read key and value
				String key = Objects.toString(e.getKey(), null);
				String val = Objects.toString(e.getValue(), "");
				
				// Ignore nulls
				if (key == null || key.equalsIgnoreCase("Manifest-Version"))
					continue;
				
				// Put
				input.put(key, val);
			}
			
			// Already translated? ignore
			if (input.containsKey("X-SquirrelJME-SourceName"))
				return;
			
			// Go through all input entries
			int depnum = 1;
			for (Map.Entry<String, String> e : input.entrySet())
			{
				String key = e.getKey();
				String val = e.getValue();
				
				// Depends on the key
				switch (key.toLowerCase())
				{
						// Library name
					case "liblet-name":
						output.put("X-SquirrelJME-Title", val);
						break;
						
						// Library Description
					case "liblet-description":
						output.put("X-SquirrelJME-Description", val);
						break;
						
						// Vendor
					case "liblet-vendor":
						output.put("X-SquirrelJME-Vendor", val);
						break;
						
						// Version
					case "liblet-version":
						output.put("X-SquirrelJME-Version", val);
						break;
						
						// Dependenceis
					case "x-squirreljme-depends":
						for (String dep : val.split("[ \t]"))
							output.put(String.format("LIBlet-Dependency-%d:",
								depnum++), String.format(
								"x-squirreljme;required;unknown@%s;;", dep));
						break;
					
						// Default is to copy
					default:
						output.put(key, val);
						break;
				}
			}
			
			// Print output
			System.out.println("Manifest-Version: 1.0");
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> e : output.entrySet())
			{
				// Build full output
				sb.setLength(0);
				sb.append(e.getKey());
				sb.append(": ");
				sb.append(e.getValue());
				
				// Print it
				System.out.println(sb);
			}
			
			throw new Error("TODO");
		}
	}
}

