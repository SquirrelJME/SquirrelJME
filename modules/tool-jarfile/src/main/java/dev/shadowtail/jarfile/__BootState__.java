// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;

/**
 * The boot state of the system.
 *
 * @since 2020/12/13
 */
final class __BootState__
{
	/** The class data used. */
	private final Map<ClassName, MinimizedClassFile> _classData =
		new HashMap<>();
	
	/** The name of the boot class. */
	private ClassName _bootClass;
	
	/**
	 * Adds the specified class to be loaded and handled later.
	 * 
	 * @param __in The input class data.
	 * @param __isBootClass Is this the boot class?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/13
	 */
	public void addClass(InputStream __in, boolean __isBootClass)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Load and store class
		MinimizedClassFile loaded = MinimizedClassFile.decode(__in);
		this._classData.put(loaded.thisName(), loaded);
		
		// Is this the entry class?
		if (__isBootClass)
			this._bootClass = loaded.thisName();
	}
}
