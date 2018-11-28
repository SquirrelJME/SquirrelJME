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

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the accelerator table found in PCF fonts, just general
 * information on the font and such.
 *
 * @since 2018/11/27
 */
final class __PCFAccelerators__
{
	/** Accelerators with ink bounds. */
	static final int _PCF_ACCEL_W_INKBOUNDS =
		0x00000100;
	
	/** The format. */
	final int _format;
	
	/** No overlap? */
	final boolean _nooverlap;
	
	/** Constant metrics? */
	final boolean _constantmetrics;
	
	/** Terminal font? */
	final boolean _terminalfont;
	
	/** Constant width? */
	final boolean _constantwidth;
	
	/** Ink inside the bounds? */
	final boolean _inkinside;
	
	/** Do the ink metrics differ ever? */
	final boolean _inkmetrics;
	
	/** Draw direction, false=LTR, true=RTL. */
	final boolean _drawdirection;
	
	/** Ascent. */
	final int _ascent;
	
	/** Descent. */
	final int _descent;
	
	/** Maximum overlap. */
	final int _maxoverlap;
	
	/** Minimum bounds. */
	final __PCFMetric__ _minbounds;
	
	/** Maximum bounds. */
	final __PCFMetric__ _maxbounds;
	
	/** Minimum ink bounds, is optional. */
	final __PCFMetric__ _inkminbounds;
	
	/** Maximum ink bounds, is optional. */
	final __PCFMetric__ _inkmaxbounds;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the accelerators.
	 *
	 * @param __format Format
	 * @param __nooverlap No overlap?
	 * @param __constantmetrics Constant metrics?
	 * @param __terminalfont Terminal font?
	 * @param __constantwidth Constant width?
	 * @param __inkinside All inks inside?
	 * @param __inkmetrics Ink metrics?
	 * @param __drawdirection Draw direction LTR or RTL?
	 * @param __ascent Ascent.
	 * @param __descent Descent.
	 * @param __maxoverlap Maximum overlap.
	 * @param __minbounds Minimum bounds.
	 * @param __maxbounds Maximum bounds.
	 * @param __inkminbounds Minimum ink bounds.
	 * @param __inkmaxbounds Maximum ink bounds.
	 * @throws NullPointerException If no min bounds or max bounds were
	 * specified.
	 * @since 2018/11/27
	 */
	__PCFAccelerators__(int __format, boolean __nooverlap,
		boolean __constantmetrics, boolean __terminalfont,
		boolean __constantwidth, boolean __inkinside, boolean __inkmetrics,
		boolean __drawdirection, int __ascent, int __descent, int __maxoverlap,
		__PCFMetric__ __minbounds, __PCFMetric__ __maxbounds,
		__PCFMetric__ __inkminbounds, __PCFMetric__ __inkmaxbounds)
		throws NullPointerException
	{
		if (__minbounds == null || __maxbounds == null)
			throw new NullPointerException("NARG");
		
		this._format = __format;
		this._nooverlap = __nooverlap;
		this._constantmetrics = __constantmetrics;
		this._terminalfont = __terminalfont;
		this._constantwidth = __constantwidth;
		this._inkinside = __inkinside;
		this._inkmetrics = __inkmetrics;
		this._drawdirection = __drawdirection;
		this._ascent = __ascent;
		this._descent = __descent;
		this._maxoverlap = __maxoverlap;
		this._minbounds = __minbounds;
		this._maxbounds = __maxbounds;
		this._inkminbounds = __inkminbounds;
		this._inkmaxbounds = __inkmaxbounds;
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
			this._string = new WeakReference<>((rv = String.format("{" +
				"_format=%x, " +
				"_nooverlap=%s, " +
				"_constantmetrics=%s, " +
				"_terminalfont=%s, " +
				"_constantwidth=%s, " +
				"_inkinside=%s, " +
				"_inkmetrics=%s, " +
				"_drawdirection=%s, " +
				"_ascent=%s, " +
				"_descent=%s, " +
				"_maxoverlap=%s, " +
				"_minbounds=%s, " +
				"_maxbounds=%s, " +
				"_inkminbounds=%s, " +
				"_inkmaxbounds=%s" +
				"}", this._format,
				this._nooverlap,
				this._constantmetrics,
				this._terminalfont,
				this._constantwidth,
				this._inkinside,
				this._inkmetrics,
				this._drawdirection,
				this._ascent,
				this._descent,
				this._maxoverlap,
				this._minbounds,
				this._maxbounds,
				this._inkminbounds,
				this._inkmaxbounds)));
		
		return rv;
	}
	
	/**
	 * Reads the accelerator data.
	 *
	 * @param __dis The input stream to parse from.
	 * @return The accelerator table.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/27
	 */
	static final __PCFAccelerators__ __read(DataInputStream __dis)
		throws IOException, NullPointerException
	{
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read the format
		int format = Integer.reverseBytes(__dis.readInt());
		
		// Fields which might be true for all characters
		boolean nooverlap = (__dis.readByte() != 0);
		boolean constantmetrics = (__dis.readByte() != 0);
		boolean terminalfont = (__dis.readByte() != 0);
		boolean constantwidth = (__dis.readByte() != 0);
		boolean inkinside = (__dis.readByte() != 0);
		boolean inkmetrics = (__dis.readByte() != 0);
		boolean drawdirection = (__dis.readByte() != 0);
		
		// Padding byte
		__dis.readByte();
		
		// Ascent, descent, and max overlap
		int ascent = __dis.readInt();
		int descent = __dis.readInt();
		int maxoverlap = __dis.readInt();
		
		// Read minimum and maximum bounds
		__PCFMetric__ minbounds = __PCFMetric__.__readUncompressed(__dis);
		__PCFMetric__ maxbounds = __PCFMetric__.__readUncompressed(__dis);
		
		// These are optional only if the given format is used
		__PCFMetric__ inkminbounds = null,
			inkmaxbounds = null;
		if ((format & _PCF_ACCEL_W_INKBOUNDS) != 0)
		{
			inkminbounds = __PCFMetric__.__readUncompressed(__dis);
			inkmaxbounds = __PCFMetric__.__readUncompressed(__dis);
		}
		
		return new __PCFAccelerators__(format, nooverlap, constantmetrics,
			terminalfont, constantwidth, inkinside, inkmetrics, drawdirection,
			ascent, descent, maxoverlap, minbounds, maxbounds, inkminbounds,
			inkmaxbounds);
	}
}

