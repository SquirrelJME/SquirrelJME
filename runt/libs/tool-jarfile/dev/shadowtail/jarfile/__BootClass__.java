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
 * This class contains boot class information.
 *
 * @since 2019/04/27
 */
final class __BootClass__
{
	/** The minimized class. */
	public final MinimizedClassFile minicf;
	
	/** Minimized class offset. */
	public final int jaroffset;
	
	/** The super class of this boot class. */
	public __BootClass__ superclass;
	
	/** Available interfaces. */
	public __BootClass__[] interfaces;
	
	/** The location of the pool pointer. */
	public int poolptr;
	
	/**
	 * Initializes the boot class.
	 *
	 * @param __mcf Mini class file.
	 * @param __off Offset to the mini-class in the Jar data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	__BootClass__(MinimizedClassFile __mcf, int __off)
		throws NullPointerException
	{
		if (__mcf == null)
			throw new NullPointerException("NARG");
		
		this.minicf = __mcf;
		this.jaroffset = __off;
	}
}

