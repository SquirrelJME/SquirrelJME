// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

/**
 * This contains some common data sources.
 *
 * @since 2017/03/28
 */
public class CommonData
{
	/**
	 * Obtains a key or value.
	 *
	 * @param __Val Is a value obtained?
	 * @param __i The index of the value or key.
	 * @return The key, value, or {@code null} if none is associated with the
	 * index.
	 * @since 2017/03/28
	 */
	public static String __keyValue(boolean __val, int __i)
	{
		// Value, can be any case
		if (__val)
			switch (__i)
			{
				case 0: return "AreAwesome";
				case 1: return "IsAGraySquirrel";
				case 2: return "ICsOfTheEras";
				case 3: return "IsLegal";
				case 4: return "CoyoteOfTheBlueSky";
				case 5: return "MakeItLegal";
				case 6: return "bbbbbbbbbb";
				default: return null;
			}
		
		// Key, only lowercase
		else
			switch (__i)
			{
				case 0: return "squirrels";
				case 1: return "xer";
				case 2: return "nekoed";
				case 3: return "quaa";
				case 4: return "mojavekoyote";
				case 5: return "awoo";
				case 6: return "aaaaaaaaaa";
				default: return null;
			}
	}
}

