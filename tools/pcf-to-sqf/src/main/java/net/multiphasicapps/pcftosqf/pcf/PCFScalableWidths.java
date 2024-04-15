// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf.pcf;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Scalable widths table, these are postscript em-units at 1/1000th of an em.
 *
 * @since 2018/11/28
 */
public final class PCFScalableWidths
{
	/** The format. */
	public final int format;
	
	/** The scalable widths. */
	private final int[] _swidths;
	
	/**
	 * Initializes the scalable widths.
	 *
	 * @param __format The format used.
	 * @param __swidths The widths.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public PCFScalableWidths(int __format, int[] __swidths)
		throws NullPointerException
	{
		if (__swidths == null)
			throw new NullPointerException("NARG");
		
		this.format = __format;
		this._swidths = __swidths.clone();
	}
	
	/**
	 * Reads the scalable widths.
	 *
	 * @param __dis The input stream.
	 * @return The resulting encoding data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public static final PCFScalableWidths read(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Format
		int format = Integer.reverseBytes(__dis.readInt());
		
		// Read in table
		int n = __dis.readInt();
		int[] swidths = new int[n];
		for (int i = 0; i < n; i++)
			swidths[i] = __dis.readInt();
		
		return new PCFScalableWidths(format, swidths);
	}
}

