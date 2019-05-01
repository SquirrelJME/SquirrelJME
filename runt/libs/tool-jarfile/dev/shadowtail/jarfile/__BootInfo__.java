// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import dev.shadowtail.classfile.mini.MinimizedClassFile;

/**
 * Boot information for a class.
 *
 * @since 2019/04/30
 */
final class __BootInfo__
{
	/** The minimized class. */
	final MinimizedClassFile _class;
	
	/** The offset to the class. */
	final int _classoffset;
	
	/** The offset to the constant pool allocation. */
	int _pooloffset;
	
	/** The base of the class. */
	int _ibase;
	
	/** The instance size of the class. */
	int _isize;
	
	/** The ID of this class. */
	int _id;
	
	/** The base of the vtable. */
	int _mbase;
	
	/** The size of the vtable. */
	int _msize;
	
	/**
	 * Initializes the boot info.
	 *
	 * @param __cl The class.
	 * @param __co The class offset.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	__BootInfo__(MinimizedClassFile __cl, int __co)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this._class = __cl;
		this._classoffset = __co;
	}
}

