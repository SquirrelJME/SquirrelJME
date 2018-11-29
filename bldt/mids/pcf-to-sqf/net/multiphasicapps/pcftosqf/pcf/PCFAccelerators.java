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

/**
 * This represents the accelerator table found in PCF fonts, just general
 * information on the font and such.
 *
 * @since 2018/11/27
 */
public final class PCFAccelerators
{
	/** Accelerators with ink bounds. */
	static final int _PCF_ACCEL_W_INKBOUNDS =
		0x00000100;
	
	/** The format. */
	public final int format;
	
	/** No overlap? */
	public final boolean nooverlap;
	
	/** Constant metrics? */
	public final boolean constantmetrics;
	
	/** Terminal font? */
	public final boolean terminalfont;
	
	/** Constant width? */
	public final boolean constantwidth;
	
	/** Ink inside the bounds? */
	public final boolean inkinside;
	
	/** Do the ink metrics differ ever? */
	public final boolean inkmetrics;
	
	/** Draw direction, false=LTR, true=RTL. */
	public final boolean drawdirection;
	
	/** Ascent. */
	public final int ascent;
	
	/** Descent. */
	public final int descent;
	
	/** Maximum overlap. */
	public final int maxoverlap;
	
	/** Minimum bounds. */
	public final PCFMetric minbounds;
	
	/** Maximum bounds. */
	public final PCFMetric maxbounds;
	
	/** Minimum ink bounds, is optional. */
	public final PCFMetric inkminbounds;
	
	/** Maximum ink bounds, is optional. */
	public final PCFMetric inkmaxbounds;
	
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
	PCFAccelerators(int __format, boolean __nooverlap,
		boolean __constantmetrics, boolean __terminalfont,
		boolean __constantwidth, boolean __inkinside, boolean __inkmetrics,
		boolean __drawdirection, int __ascent, int __descent, int __maxoverlap,
		PCFMetric __minbounds, PCFMetric __maxbounds,
		PCFMetric __inkminbounds, PCFMetric __inkmaxbounds)
		throws NullPointerException
	{
		if (__minbounds == null || __maxbounds == null)
			throw new NullPointerException("NARG");
		
		this.format = __format;
		this.nooverlap = __nooverlap;
		this.constantmetrics = __constantmetrics;
		this.terminalfont = __terminalfont;
		this.constantwidth = __constantwidth;
		this.inkinside = __inkinside;
		this.inkmetrics = __inkmetrics;
		this.drawdirection = __drawdirection;
		this.ascent = __ascent;
		this.descent = __descent;
		this.maxoverlap = __maxoverlap;
		this.minbounds = __minbounds;
		this.maxbounds = __maxbounds;
		this.inkminbounds = __inkminbounds;
		this.inkmaxbounds = __inkmaxbounds;
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
				"format=%x, " +
				"nooverlap=%s, " +
				"constantmetrics=%s, " +
				"terminalfont=%s, " +
				"constantwidth=%s, " +
				"inkinside=%s, " +
				"inkmetrics=%s, " +
				"drawdirection=%s, " +
				"ascent=%s, " +
				"descent=%s, " +
				"maxoverlap=%s, " +
				"minbounds=%s, " +
				"maxbounds=%s, " +
				"inkminbounds=%s, " +
				"inkmaxbounds=%s" +
				"}", this.format,
				this.nooverlap,
				this.constantmetrics,
				this.terminalfont,
				this.constantwidth,
				this.inkinside,
				this.inkmetrics,
				this.drawdirection,
				this.ascent,
				this.descent,
				this.maxoverlap,
				this.minbounds,
				this.maxbounds,
				this.inkminbounds,
				this.inkmaxbounds)));
		
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
	public static final PCFAccelerators read(DataInputStream __dis)
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
		PCFMetric minbounds = PCFMetric.readUncompressed(__dis);
		PCFMetric maxbounds = PCFMetric.readUncompressed(__dis);
		
		// These are optional only if the given format is used
		PCFMetric inkminbounds = null,
			inkmaxbounds = null;
		if ((format & _PCF_ACCEL_W_INKBOUNDS) != 0)
		{
			inkminbounds = PCFMetric.readUncompressed(__dis);
			inkmaxbounds = PCFMetric.readUncompressed(__dis);
		}
		
		return new PCFAccelerators(format, nooverlap, constantmetrics,
			terminalfont, constantwidth, inkinside, inkmetrics, drawdirection,
			ascent, descent, maxoverlap, minbounds, maxbounds, inkminbounds,
			inkmaxbounds);
	}
}

