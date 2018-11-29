// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf.pcf;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * This contains a single metric within a PCF file.
 *
 * @since 2018/11/27
 */
public final class PCFMetric
{
	/** Compressed metrics format. */
	static final int _PCF_COMPRESSED_METRICS =
		0x00000100;
	
	/** Left side bearing. */
	public final short leftsidebearing;
	
	/** Right side bearing. */
	public final short rightsidebearing;
	
	/** Character width. */
	public final short charwidth;
	
	/** Character ascent. */
	public final short charascent;
	
	/** Character descent. */
	public final short chardescent;
	
	/** Character attributes. */
	public final int attributes;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the metric.
	 *
	 * @param __lsb Left side bearing.
	 * @param __rsb Right side bearing.
	 * @param __cw Character width.
	 * @param __ca Character ascent.
	 * @param __cd Character descent.
	 * @param __a Attributes.
	 * @since 2018/11/27
	 */
	PCFMetric(short __lsb, short __rsb, short __cw, short __ca,
		short __cd, int __a)
	{
		this.leftsidebearing = __lsb;
		this.rightsidebearing = __rsb;
		this.charwidth = __cw;
		this.charascent = __ca;
		this.chardescent = __cd;
		this.attributes = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/27
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"{leftsidebearing=%d, rightsidebearing=%d, " +
				"charwidth=%d, charascent=%d, chardescent=%d, " +
				"attributes=%x}", this.leftsidebearing,
				this.rightsidebearing, this.charwidth, this.charascent,
				this.chardescent, this.attributes)));
		
		return rv;
	}
	
	/**
	 * Reads a compressed metric from the input.
	 *
	 * @param __dis The stream to read from.
	 * @return The metric.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public static final PCFMetric readCompressed(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// All are unsigned bytes with implied attributes of zero
		// All of the values are offset signed
		return new PCFMetric(
			(short)(__dis.readUnsignedByte() - 0x80),
			(short)(__dis.readUnsignedByte() - 0x80),
			(short)(__dis.readUnsignedByte() - 0x80),
			(short)(__dis.readUnsignedByte() - 0x80),
			(short)(__dis.readUnsignedByte() - 0x80),
			0);
	}
	
	/**
	 * Reads the metric table.
	 *
	 * @param __dis The stream to read from.
	 * @return The read metrics.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public static final PCFMetric[] readMetrics(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		PCFMetric[] rv;
		
		// Are these compressed metrics
		if (((Integer.reverseBytes(__dis.readInt())) &
			_PCF_COMPRESSED_METRICS) != 0)
		{
			// Read length
			int n = __dis.readUnsignedShort();
			rv = new PCFMetric[n];
			
			// Read all metrics			
			for (int i = 0; i < n; i++)
				rv[i] = PCFMetric.readCompressed(__dis);
		}
		
		// They are uncompressed
		else
		{
			// Read length
			int n = __dis.readInt();
			rv = new PCFMetric[n];
			
			// Read all metrics			
			for (int i = 0; i < n; i++)
				rv[i] = PCFMetric.readUncompressed(__dis);
		}
		
		// Debug
		todo.DEBUG.note("Metrics=%s", Arrays.asList(rv));
		
		return rv;
	}
	
	/**
	 * Reads an uncompressed metric from the input.
	 *
	 * @param __dis The stream to read from.
	 * @return The metric.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	public static final PCFMetric readUncompressed(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// All are unsigned bytes with implied attributes of zero
		return new PCFMetric(
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readUnsignedShort());
	}
}

