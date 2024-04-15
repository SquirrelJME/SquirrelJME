// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.txttopbm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.LinkedList;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This class is used to convert a text file to a PBM.
 *
 * @since 2019/06/15
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments.
	 * @since 2019/06/15
	 */
	public static final void main(String... __args)
		throws Throwable
	{
		// Read in strings, find maximum length
		int maxstrlen = 0,
			numlines = 0;
		Deque<String> lines = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
			Files.newInputStream(Paths.get(__args[0]),
			StandardOpenOption.READ))))
		{
			for (;;)
			{
				String ln = br.readLine();
				
				if (ln == null)
					break;
				
				// Add line
				lines.add(ln);
				numlines++;
				
				// Maximum line size
				int strlen = ln.length();
				if (strlen > maxstrlen)
					maxstrlen = strlen;
			}
		}
		
		// Write output graphics file, line by line!
		try (PrintStream ps = new PrintStream(System.out))
		{
			// Get a large monospace font which is easy to read hopefully
			Font font = Font.getFont(Font.FACE_MONOSPACE, 0, Font.SIZE_LARGE);
			
			// Get the size properties of the font
			int fw = (font.charWidth('a') + font.charWidth('w')) >> 1,
				fh = font.getHeight();
				
			// Calculate size of image, width is rounded to 8 because of
			// the pixel packing!
			int iw = ((maxstrlen * fw) + 7) & (~7),
				ih = numlines * fh,
				sa = iw * fh,
				ua = sa / 8;
			
			// Write PBM header, use P4 format
			ps.print("P4\n");
			ps.printf("%d %d\n", iw, ih);
			
			// Create image to contain a buffer for a single line, it is not
			// important to have an entire image because we can just pipe
			// this out!
			Image image = Image.createImage(iw, fh);
			Graphics g = image.getGraphics();
			
			// Use the monospace font!
			g.setFont(font);
			
			// RGB pixel output for lines
			int[] rgb = new int[sa];
			
			// Bulk byte data, for the fastest possible writing
			byte[] bulk = new byte[ua];
			
			// Used to keep track of the current column since PBM cannot exceed
			// 72 characters
			int col = 1;
			
			// Process each line!
			while (!lines.isEmpty())
			{
				// Read in line
				String ln = lines.removeFirst();
				
				// Clear background with white
				g.setColor(0xFFFFFF);
				g.fillRect(0, 0, iw, fh);
				
				// Draw text in black
				g.setColor(0x000000);
				g.drawString(ln, 0, 0, Graphics.TOP | Graphics.LEFT);
				
				// Get RGB pixel data
				image.getRGB(rgb, 0, iw, 0, 0, iw, fh);
				
				// Go through pixels and export to bulk format
				for (int i = 0, o = 0; i < sa; i += 8, o++)
					bulk[o] = (byte)(((rgb[i + 0] & 1) << 7) |
						((rgb[i + 1] & 1) << 6) |
						((rgb[i + 2] & 1) << 5) |
						((rgb[i + 3] & 1) << 4) |
						((rgb[i + 4] & 1) << 3) |
						((rgb[i + 5] & 1) << 2) |
						((rgb[i + 6] & 1) << 1) |
						((rgb[i + 7] & 1)));
				
				// Write all bytes at once
				ps.write(bulk);
			}
		}
	}
}

