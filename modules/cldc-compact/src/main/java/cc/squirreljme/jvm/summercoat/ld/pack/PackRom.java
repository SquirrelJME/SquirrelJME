// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.pack;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.ByteOrderType;
import cc.squirreljme.jvm.summercoat.constants.ClassInfoConstants;
import cc.squirreljme.jvm.summercoat.ld.mem.RealMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is used to access the pack ROM.
 *
 * @since 2021/02/09
 */
public final class PackRom
{
	/** The base address of the ROM. */
	protected final long baseAddr;
	
	/**
	 * Initializes the pack ROM manager.
	 * 
	 * @param __memAddr The memory address where the Pack ROM is located.
	 * @since 2021/02/09
	 */
	private PackRom(long __memAddr)
	{
		this.baseAddr = __memAddr;
	}
	
	/**
	 * Returns all of the libraries that are available for the ROM.
	 * 
	 * @return The libraries.
	 * @since 2021/02/09
	 */
	public final JarPackageBracket[] libraries()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Loads the PackRom from the given address.
	 * 
	 * @param __memAddr The memory address where the ROM is located.
	 * @return The pack ROM of the given address.
	 * @since 2021/02/14
	 */
	public static PackRom load(long __memAddr)
	{
		Debugging.debugNote("Max Pack: %d", ClassInfoConstants.PACK_MAXIMUM_HEADER_SIZE);
		
		new RealMemory(__memAddr, ClassInfoConstants.PACK_MAXIMUM_HEADER_SIZE, ByteOrderType.BIG_ENDIAN);
		
		return new PackRom(__memAddr);
	}
}
