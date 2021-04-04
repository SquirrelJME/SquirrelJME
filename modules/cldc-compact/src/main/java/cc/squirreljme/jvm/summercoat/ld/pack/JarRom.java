// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.pack;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.summercoat.ld.mem.ReadableMemory;

/**
 * This represents a Jar, or some other abstract file, within the ROM.
 *
 * @since 2021/04/04
 */
public class JarRom
	implements JarPackageBracket
{
	/** The JAR data. */
	protected final ReadableMemory data;
	
	/** The JAR flags. */
	protected final int flags;
	
	/** The JAR name. */
	protected final String name;
	
	/**
	 * Initializes the Jar ROM.
	 * 
	 * @param __flags The flags for the JAR.
	 * @param __name The name of the JAR.
	 * @param __data The data for the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/04
	 */
	public JarRom(int __flags, String __name, ReadableMemory __data)
		throws NullPointerException
	{
		if (__name == null || __data == null)
			throw new NullPointerException("NARG");
		
		this.flags = __flags;
		this.name = __name;
		this.data = __data;
	}
}
