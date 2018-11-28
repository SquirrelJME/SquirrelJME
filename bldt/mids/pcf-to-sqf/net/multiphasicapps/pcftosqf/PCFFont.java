// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf;

import java.io.ByteArrayInputStream;	
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;

/**
 * This class is capable of reading PCF formatted fonts and reading all of
 * the information from it.
 *
 * Information on the font is at these locations:
 * * <https://fontforge.github.io/en-US/documentation/reference/pcf-format/>
 * * <https://web.archive.org/web/20020215194039if_/
 *   http://myhome.hananet.net:80/~bumchul/xfont/pcf.txt>
 * * <https://web.archive.org/web/20090205034052/
 *   http://www.tsg.ne.jp:80/GANA/S/pcf2bdf/pcf.pdf>
 *
 * @since 2018/11/25
 */
public class PCFFont
{
	/**
	 * Reads the given stream for PCF font information.
	 *
	 * @param __in The stream to read from.
	 * @return The decoded font information.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/25
	 */
	public static final PCFFont read(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Need to read in the data!
		DataInputStream dos = new DataInputStream(__in);
		
		// {@squirreljme.error EB2g Invalid PCF magic number.}
		int magic;
		if ((magic = dos.readInt()) != 0x01666370)
			throw new IOException(String.format("EB2g %08x", magic));
		
		// Read each table entry, since they have offsets into the file they
		// could be in any random order which would be bad
		Set<__PCFFontTable__> tables = new SortedTreeSet<>();
		int numtables = Integer.reverseBytes(dos.readInt());
		for (int i = 0; i < numtables; i++)
			tables.add(new __PCFFontTable__(
				Integer.reverseBytes(dos.readInt()),
				Integer.reverseBytes(dos.readInt()),
				Integer.reverseBytes(dos.readInt()),
				Integer.reverseBytes(dos.readInt())));
		
		// Determine the base position of the read pointer
		int readptr = 8 + (numtables * 16);
		
		// Debug
		todo.DEBUG.note("Table: %s, ended at %d", tables, readptr);
		
		// Parsed fields
		__PCFProperties__ pcfp = null;
		__PCFAccelerators__ pcfaccel = null;
		__PCFMetric__[] metrics = null;
		
		// Go through all table entries and parse them, they will be sorted
		// by their offset and handled as such
		for (__PCFFontTable__ te : tables)
		{
			// Skip bytes needed to reach the destination
			int skippy = te._offset - readptr;
			if (skippy > 0)
				dos.skipBytes(skippy);
			
			// {@squirreljme.error EB2m Negative skip distance.}
			else if (skippy < 0)
				throw new IOException("EB2m");
			
			// Read in data that makes up this section
			byte[] data = new byte[te._size];
			dos.readFully(data);
			
			// Handle the data in the section
			switch (te._type)
			{
					// Properties
				case 1:
					try (DataInputStream dis = new DataInputStream(
						new ByteArrayInputStream(data)))
					{
						pcfp = __PCFProperties__.__read(dis);
					}
					break;
					
					// Accelerators
				case 2:
					try (DataInputStream dis = new DataInputStream(
						new ByteArrayInputStream(data)))
					{
						pcfaccel = __PCFAccelerators__.__read(dis);
						
						// Debug
						todo.DEBUG.note("Accelerators -- %s", pcfaccel);
					}
					break;
					
					// Metrics
				case 4:
					try (DataInputStream dis = new DataInputStream(
						new ByteArrayInputStream(data)))
					{
						metrics = __PCFMetric__.__readMetrics(dis);
					}
					break;
					
					// Bitmaps
				case 8:
					try (DataInputStream dis = new DataInputStream(
						new ByteArrayInputStream(data)))
					{
						if (true)
							throw new todo.TODO();
					}
					break;
					
					// Ink Metrics
				case 16:
					if (true)
						throw new todo.TODO();
					break;
					
					// BDF Encodings
				case 32:
					if (true)
						throw new todo.TODO();
					break;
					
					// SWidths
				case 64:
					if (true)
						throw new todo.TODO();
					break;
					
					// Glyph Names
				case 128:
					if (true)
						throw new todo.TODO();
					break;
					
					// BDF Accelerators
				case 256:
					if (true)
						throw new todo.TODO();
					break;
					
					// {@squirreljme.error EB2l Unknown PCF type. (The type)}
				default:
					throw new IOException("EB2l " + te._type);
			}
			
			// Set pointer for next run
			readptr += te._size;
		}
		
		throw new todo.TODO();
	}
}

