// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.IOException;

/**
 * Control for frames within GIFs.
 *
 * @since 2022/07/07
 */
final class __GIFFrameControl__
{
	/** The mask for the disposal method. */
	private static final byte _DISPOSAL_METHOD_MASK =
		0b000_111_00;
	
	/** The shift for the disposal method. */
	private static final byte _DISPOSAL_METHOD_SHIFT =
		2;
	
	/** The bit for user input. */
	private static final byte _USER_INPUT_BIT =
		0b000_000_10;
	
	/** The bit for the transparency flag. */
	private static final byte _TRANS_COLOR_BIT =
		0b000_000_01;
	
	/** The delay in milliseconds. */
	public final int delayMilli;
	
	/** The transparent color index. */
	public final int transColor;
	
	/** The disposal method for frames. */
	public final __GIFDisposeMethod__ disposeMethod;
	
	/** Has user input? */
	public final boolean hasUserInput;
	
	/** Has transparent color? */
	public final boolean hasTransColor;
	
	/**
	 * Initializes the frame control information.
	 * 
	 * @param __flags The flags used.
	 * @param __delayTime The delay time before this appears.
	 * @param __transDx The transparent color index.
	 * @throws IOException If the fields are invalid.
	 * @since 2022/07/07
	 */
	__GIFFrameControl__(int __flags, int __delayTime, int __transDx)
		throws IOException
	{
		// Parameters within bounds?
		if (__delayTime < 0 || __delayTime > 65535 ||
			__transDx < 0 || __transDx > 255)
			throw new IOException("IOOB");
		
		// Obtain the disposal method
		int rawDisposeMethod =
			((__flags & __GIFFrameControl__._DISPOSAL_METHOD_MASK) >>
				__GIFFrameControl__._DISPOSAL_METHOD_SHIFT);
		this.disposeMethod = __GIFDisposeMethod__.valueOf(rawDisposeMethod);
		
		// Transparent color
		this.transColor = __transDx;
		this.hasTransColor =
			(0 != (__flags & __GIFFrameControl__._TRANS_COLOR_BIT));
		
		// Calculate delay in milliseconds, the original unit is 1/100 seconds
		// or centiseconds... interesting.
		this.delayMilli = Math.max(0, __delayTime * 10);
		
		// User input? Which is not used at all in SquirrelJME
		this.hasUserInput =
			(0 != (__flags & __GIFFrameControl__._USER_INPUT_BIT));
	}
}
