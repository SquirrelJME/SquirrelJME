// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

/**
 * The flags which represent GIF files.
 *
 * @since 2022/06/26
 */
final class __GIFFlags__
{
	/** Mask for GCT. */
	private static final short _HAS_GCT_MASK =
		0b1000_0000;
	
	/** Mask for color resolution. */
	private static final byte _COLOR_RESOLUTION_MASK =
		0b0111_0000;
	
	/** Shift for color resolution. */
	private static final byte _COLOR_RESOLUTION_SHIFT =
		4;
	
	/** Sorted GCT? */
	private static final byte _SORTED_GCT =
		0b0000_1000;
	
	/** Size of the global color table. */
	private static final byte _COLOR_TABLE_SIZE =
		0b0000_0111;
	
	/** Does this GIF have a global color table? */
	public final boolean hasGlobalColorTable;
	
	/** The color resolution of the GIF. */
	public final int colorResolution;
	
	/** Is the global color table, sorted? */
	public final boolean sortedGlobalColorTable;
	
	/** The size of the global color table. */
	public final int globalColorTableSize;
	
	/**
	 * Initializes and parses the GIF Flags.
	 * 
	 * @param __flags The raw flags.
	 * @since 2022/06/26
	 */
	__GIFFlags__(int __flags)
	{
		this.hasGlobalColorTable =
			(__flags & __GIFFlags__._HAS_GCT_MASK) != 0;
		this.colorResolution = ((__flags & __GIFFlags__._COLOR_RESOLUTION_MASK)
			>>> __GIFFlags__._COLOR_RESOLUTION_SHIFT) - 1;
		this.sortedGlobalColorTable =
			(__flags & __GIFFlags__._SORTED_GCT) != 0;
		this.globalColorTableSize =
			1 << ((__flags & __GIFFlags__._COLOR_TABLE_SIZE) + 1);
	}
}
