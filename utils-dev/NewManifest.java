// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.jar.Manifest;
import java.util.List;
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
	/** Fixed sorting order. */
	public static final List<String> FIXED_SORT_ORDER =
		Arrays.<String>asList("X-SquirrelJME-SourceName", "X-SquirrelJME-Type",
			"X-SquirrelJME-Title", "X-SquirrelJME-Vendor",
			"X-SquirrelJME-Version", "X-SquirrelJME-Description");
	
	/** The column limit. */
	public static final int COLUMN_LIMIT =
		72;
	
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
				// Sort SquirrelJME specific before non-specific
				boolean xsqa = __a.startsWith("X-SquirrelJME-"),
					xsqb = __b.startsWith("X-SquirrelJME-");
				if (xsqa && !xsqb)
					return -1;
				else if (!xsqa && xsqb)
					return 1;
				
				// In fixed order?
				int fixa = FIXED_SORT_ORDER.indexOf(__a),
					fixb = FIXED_SORT_ORDER.indexOf(__b);
				if (fixa >= 0 && fixb >= 0)
				{
					if (fixa < fixb)
						return -1;
					else if (fixa > fixb)
						return 1;
				}
				
				// Specific ones before non-specific
				else if (fixa >= 0 && fixb < 0)
					return -1;
				
				// non-specific after specific ones
				else if (fixa < 0 && fixb >= 0)
					return 1;
				
				// Get length of both
				int la = __a.length(), lb = __b.length();
				int mm = Math.min(la, lb);
				
				// Compare each character
				int ia = 0, ib = 0;
				for (; ia < la && ib < lb;)
				{
					// Read both characters
					char ca = __a.charAt(ia++),
						cb = __b.charAt(ib++);
					
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
							return rv;
					}
					
					// Both digits
					else
					{
						// Get digit to use
						int numa = Character.digit(ca, 10),
							numb = Character.digit(cb, 10);
						
						// Read number of the first string
						while (ia < la)
						{
							char peek = __a.charAt(ia);
							
							// Stop if not a digit
							if (!Character.isDigit(peek))
								break;
							
							// Add into it
							numa = (numa * 10) + Character.digit(peek, 10);
							ia++;
						}
						
						// Read number of the second string
						while (ib < lb)
						{
							char peek = __b.charAt(ib);
							
							// Stop if not a digit
							if (!Character.isDigit(peek))
								break;
							
							// Add into it
							numb = (numb * 10) + Character.digit(peek, 10);
							ib++;
						}
						
						// Compare
						if (numa < numb)
							return -1;
						else if (numa > numb)
							return 1;
					}
				}
				
				// Characters left?
				boolean lefta = ia < la,
					leftb = ib < lb;
				
				// Shorter is lower
				if (!lefta && leftb)
					return -1;
				
				// Longer is higher
				else if (lefta && !leftb)
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
				throw new RuntimeException("AlreadyTranslated");
			
			// Force everything to be a liblet initially
			output.put("X-SquirrelJME-Type", "liblet");
			
			// Go through all input entries
			int depnum = 1;
			for (Map.Entry<String, String> e : input.entrySet())
			{
				String key = e.getKey();
				String val = e.getValue();
				
				// Depends on the key
				switch (key.toLowerCase())
				{
						// Project name
					case "x-squirreljme-name":
						output.put("X-SquirrelJME-SourceName", val);
						break;
					
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
							output.put(String.format(
								"X-SquirrelJME-Dependency-%d", depnum++),
								String.format("liblet:unknown@%s", dep));
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
				
				// Print out character by character, going to the next line
				// when applicable
				String ss = sb.toString();
				int n = ss.length();
				int col = 0;
				for (int i = 0; i < n; i++)
				{
					// Get the next space
					int ns = ss.indexOf(' ', i);
					
					// If this word exceeds the column bounds then go to the
					// next line
					// Or if the column limit is reached
					if (col + (ns - i) > COLUMN_LIMIT || col >= COLUMN_LIMIT)
					{
						System.out.println();
						System.out.print(" ");
						col = 1;
					}
					
					// Print it
					System.out.print(ss.charAt(i));
					col++;
				}
				
				// Go to next line always
				System.out.println();
			}
		}
	}
}

