// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * Generates bitlines.
 *
 * @since 2024/05/17
 */
public class Bitlines
{
	/**
	 * Generates bitlines for a sequence.
	 *
	 * @param __seq The sequence to generate for.
	 * @since 2024/05/17
	 */
	public static void generate(int __seq)
	{
		int base = -1;
		int len = -1;
		
		System.out.printf("SJME_BITLINE_BEGIN(%d)\n", __seq);
		System.out.printf("{\n");
		
		System.out.printf("\tSJME_BITLINE_DEF;\n", base);
		System.out.printf("\t\n", base);
		
		for (int sh = 0, b = 1; sh <= 8; sh++, b <<= 1)
		{
			// End of seq for draw?
			if (sh == 8 || (__seq & b) == 0)
			{
				// Nothing to draw?
				if (base < 0 || len < 0)
					continue;
				
				// Output
				if (len == 1)
					System.out.printf("\tSJME_BITPIXL(%d);\n", base);
				else
					System.out.printf("\tSJME_BITLINE(%d, %d);\n", base, len);
				
				// Clear
				base = -1;
				len = -1;
			}
			else if ((__seq & b) != 0)
			{
				// Need to set base?
				if (base < 0)
				{
					base = sh;
					len = 0;
				}
				
				// Increment length up
				len++;
			}
		}
		
		System.out.printf("\t\n", base);
		System.out.printf("\tSJME_BITLINE_RET;\n", base);
		
		System.out.printf("}\n");
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2024/05/17
	 */
	public static void main(String... __args)
	{
		for (int i = 0; i < 256; i++)
		{
			StringBuilder bin = new StringBuilder(Integer.toString(i, 2));
			while (bin.length() < 8)
				bin.insert(0, '0');
			
			System.out.printf("/** Bitline %d (%8s). */\n",
				i, bin);
			
			Bitlines.generate(i);
			
			System.out.printf("\n");
		}
		
		System.out.printf("\n");
		System.out.printf("SJME_BITLINE_SET =\n");
		System.out.printf("{\n");
		for (int i = 0; i < 256; i++)
			System.out.printf("\tSJME_BITLINE_SET_USE(%d),\n", i);
		System.out.printf("};\n");
	}
}
