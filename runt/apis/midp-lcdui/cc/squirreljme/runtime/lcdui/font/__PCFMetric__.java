// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.font;

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
final class __PCFMetric__
{
	/** Compressed metrics format. */
	static final int _PCF_COMPRESSED_METRICS =
		0x00000100;
	
	/** Left side bearing. */
	final short _leftsidebearing;
	
	/** Right side bearing. */
	final short _rightsidebearing;
	
	/** Character width. */
	final short _charwidth;
	
	/** Character ascent. */
	final short _charascent;
	
	/** Character descent. */
	final short _chardescent;
	
	/** Character attributes. */
	final int _attributes;
	
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
	__PCFMetric__(short __lsb, short __rsb, short __cw, short __ca,
		short __cd, int __a)
	{
		this._leftsidebearing = __lsb;
		this._rightsidebearing = __rsb;
		this._charwidth = __cw;
		this._charascent = __ca;
		this._chardescent = __cd;
		this._attributes = __a;
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
				"attributes=%x}", this._leftsidebearing,
				this._rightsidebearing, this._charwidth, this._charascent,
				this._chardescent, this._attributes)));
		
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
	static final __PCFMetric__ __readCompressed(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// All are unsigned bytes with implied attributes of zero
		// All of the values are offset signed
		return new __PCFMetric__(
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
	static final __PCFMetric__[] __readMetrics(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		__PCFMetric__[] rv;
		
		// Are these compressed metrics
		if (((Integer.reverseBytes(__dis.readInt())) &
			_PCF_COMPRESSED_METRICS) != 0)
		{
			// Read length
			int n = __dis.readUnsignedShort();
			rv = new __PCFMetric__[n];
			
			// Read all metrics			
			for (int i = 0; i < n; i++)
				rv[i] = __PCFMetric__.__readCompressed(__dis);
		}
		
		// They are uncompressed
		else
		{
			// Read length
			int n = __dis.readInt();
			rv = new __PCFMetric__[n];
			
			// Read all metrics			
			for (int i = 0; i < n; i++)
				rv[i] = __PCFMetric__.__readUncompressed(__dis);
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
	static final __PCFMetric__ __readUncompressed(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// All are unsigned bytes with implied attributes of zero
		return new __PCFMetric__(
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readShort(),
			__dis.readUnsignedShort());
	}
}

