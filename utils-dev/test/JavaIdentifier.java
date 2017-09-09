// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This prints Java identifier information tables for a given critera.
 *
 * It is needed by the Java compiler to know which characters are valid and
 * which ones are not.
 *
 * @since 2017/09/09
 */
public class JavaIdentifier
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/09/09
	 */
	public static void main(String... __args)
	{
		int firstvalid = 0;
		boolean wasvalid = false;
		for (int i = 0; i <= 65536; i++)
		{
			// Zero is never valid
			if (i == 0)
				continue;
			
			// Is it valid?
			char c = (char)i;
			boolean nowvalid = (i <= 65535 &&
				Character.isJavaIdentifierPart(c));
			
			// Validity switch?
			if (nowvalid != wasvalid)
			{
				// Is valid now, set mark here to determine how wide this
				// range is
				if (nowvalid)
					firstvalid = i;
				
				// Is invalid now
				else
				{
					// Calculate the range of validity
					int range = i - firstvalid;
					
					// Single range, single check
					if (range == 1)
						System.out.printf("__c == 0x%04X ||%n", firstvalid);
					
					// Long range
					else
						System.out.printf(
							"(__c >= 0x%04X && __c < 0x%04X) ||%n",
							firstvalid, i);
				}
			}
			
			// Set
			wasvalid = nowvalid;
			
		}
		System.out.println();
	}
}

