// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.sqftoc;

import cc.squirreljme.runtime.lcdui.font.SQFFont;
import java.io.DataInputStream;
import java.io.PrintStream;

/**
 * Converts SQF to C file.
 *
 * @since 2019/06/20
 */
public class Main
{
	/** Column size. */
	public static final int COLUMNS =
		65;
	
	/**
	 * Main entry for the converter.
	 *
	 * @param __args Arguments, the first one is the resource to use.
	 * @throws Throwable On any exception
	 * @since 2019/06/20
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Which font to convert?
		String rcname = (__args == null || __args.length == 0 ||
			__args[0] == null ? "monospace-12.sqf" : __args[0]);
		
		// The stream to write to
		PrintStream ps = System.out;
		
		// Load the font data
		try (DataInputStream dis = new DataInputStream(
			SQFFont.class.getResourceAsStream(rcname)))
		{
			// Read fields
			byte pixelheight = dis.readByte(),
				ascent = dis.readByte(),
				descent = dis.readByte(),
				bytesperscan = dis.readByte();
			
			// The character widths
			byte[] charwidths = new byte[256];
			dis.readFully(charwidths);
			ps.println("/** SQF Character Widths. */");
			Main.__dumpBytes(ps, "sjme_fontcharwidths", charwidths);
			ps.println();
			
			// Is the character valid?
			byte[] isvalidchar = new byte[256];
			dis.readFully(isvalidchar);
			ps.println("/** SQF Character validity. */");
			Main.__dumpBytes(ps, "sjme_fontisvalidchar", isvalidchar);
			ps.println();
			
			// Character bitmaps
			byte[] charbmp = new byte[256 * bytesperscan * pixelheight];
			dis.readFully(charbmp);
			ps.println("/** SQF Character Bitmaps. */");
			Main.__dumpBytes(ps, "sjme_fontcharbmp", charbmp);
			ps.println();
			
			// Output structure for the font
			ps.println("/** SQF Defined Font. */");
			ps.println("static sjme_scritchui_sqf sjme_font =");
			ps.println("{");
			
			// All four fields in the header
			ps.printf("\t%d,%n", pixelheight);
			ps.printf("\t%d,%n", ascent);
			ps.printf("\t%d,%n", descent);
			ps.printf("\t%d,%n", bytesperscan);
			ps.println("\tsjme_fontcharwidths,");
			ps.println("\tsjme_fontisvalidchar,");
			ps.println("\tsjme_fontcharbmp");
			
			ps.println("};");
			ps.println();
		}
	}
	
	/**
	 * Dumps the specified byte array.
	 *
	 * @param __ps The stream to write to.
	 * @param __name The name of the field.
	 * @param __b The input bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/20
	 */
	private static void __dumpBytes(PrintStream __ps, String __name,
		byte[] __b)
		throws NullPointerException
	{
		if (__ps == null || __name == null || __b == null)
			throw new NullPointerException("NARG");
		
		__ps.printf("static sjme_jbyte %s[] =%n", __name);
		__ps.println("{");
		
		// Used to fill up lines at a time
		StringBuilder sb = new StringBuilder();
		
		// Print every character!
		for (int i = 0, n = __b.length; i < n; i++)
		{
			// Append byte
			sb.append(__b[i]);
			
			// Add comma if this is not the last byte
			if (i < n - 1)
				sb.append(", ");
			
			// Dump if column limit was reached
			if (sb.length() >= Main.COLUMNS)
			{
				// If it ends with a space, remove it (is annoying to have)
				if (sb.charAt(sb.length() - 1) == ' ')
					sb.setLength(sb.length() - 1);
				
				// Print out
				__ps.printf("\t%s%n", sb);
				
				// Reset for next run
				sb.setLength(0);
			}
		}
		
		// Print anything left over!
		if (sb.length() > 0)
			__ps.printf("\t%s%n", sb);
		
		__ps.println("};");
	}
}

