// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf.pcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * Properties which describe the PCF. These properties are the font atoms
 * which X11 provides to the user.
 *
 * @since 2018/11/27
 */
public final class PCFProperties
{
	/** The format of the event table. */
	public final int format;
	
	/** Values within the table. */
	public final Map<String, Object> values;
	
	/**
	 * Initializes the properties.
	 *
	 * @param __f The format.
	 * @param __v The values used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	private PCFProperties(int __f, Map<String, Object> __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this.format = __f;
		this.values = UnmodifiableMap.<String, Object>of(__v);
	}
	
	/**
	 * Reads from the input stream and loads the properties.
	 *
	 * @param __dis The input stream to read the properties from.
	 * @return The properties which have been read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public static final PCFProperties read(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read the format
		int format = Integer.reverseBytes(__dis.readInt());
		
		// Read the raw properties which can only be parsed when the string
		// table is actually read
		int nprops = __dis.readInt();
		int[] pnameoff = new int[nprops];
		boolean[] pisstr = new boolean[nprops];
		int[] pvalue = new int[nprops];
		for (int i = 0; i < nprops; i++)
		{
			pnameoff[i] = __dis.readInt();
			pisstr[i] = (__dis.readByte() != 0);
			pvalue[i] = __dis.readInt();
		}
		
		// Since the stream knows nothing of the size, it must be determined
		int eopaddr = 8 + (9 * nprops);
		
		// Skip padding bytes, which is a 4 byte boundary
		if ((eopaddr & 3) != 0)
			__dis.skipBytes(4 - (eopaddr & 3));
		
		// Read the raw string table as a bunch of bytes, these are NUL
		// terminated strings
		int ssn = __dis.readInt();
		byte[] sschars = new byte[ssn];
		__dis.readFully(sschars);
		
		// Parse the input strings and such into property values which are
		// either strings or integers
		Map<String, Object> values = new HashMap<>();
		for (int i = 0; i < nprops; i++)
		{
			String key = PCFProperties.__readString(sschars, pnameoff[i]);
			
			// Store a value into map
			values.put(key, (!pisstr[i] ? Integer.valueOf(pvalue[i]) :
				PCFProperties.__readString(sschars, pvalue[i])));
		}
		
		// Debug
		Debugging.debugNote("format=%d, props=%s", format, values);
		
		// Finish properties
		return new PCFProperties(format, values);
	}
	
	/**
	 * Reads a string from the byte array, since strings are NUL terminated
	 * they are scanned and read as such accordingly.
	 *
	 * @param __ssc Input bytes for string data.
	 * @param __p The pointer to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	static final String __readString(byte[] __ssc, int __p)
		throws NullPointerException
	{
		if (__ssc == null)
			throw new NullPointerException("NARG");
		
		// Place into string
		StringBuilder sb = new StringBuilder();
		for (int n = __ssc.length; __p < n; __p++)
		{
			// Stop at any NUL
			byte c = __ssc[__p];
			if (c == 0)
				break;
			
			sb.append((char)(c & 0xFF));
		}
		
		return sb.toString();
	}
}

