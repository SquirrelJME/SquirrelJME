// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCall;
import cc.squirreljme.jvm.memory.ReadableAssemblyMemory;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Boots the SquirrelJME virtual machine system.
 *
 * @since 2020/03/26
 */
public final class SystemBoot
{
	/**
	 * System boot entry point.
	 *
	 * @param __ramAddr The RAM address.
	 * @param __ramLen The size of RAM.
	 * @param __configAddr The configuration address.
	 * @param __configLen The configuration length.
	 * @since 2020/03/26
	 */
	@SuppressWarnings("unused")
	static void __sysBoot(long __ramAddr, int __ramLen,
		long __configAddr, int __configLen)
	{
		// Initialize the links in RAM
		Allocator.__initRamLinks(__ramAddr, __ramLen);
		
		// Setup the configuration reader to obtain our entry point info
		ConfigReader config = new ConfigReader(
			new ReadableAssemblyMemory(__configAddr, __configLen));
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
