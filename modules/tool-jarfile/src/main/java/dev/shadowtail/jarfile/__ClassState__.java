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
import net.multiphasicapps.classfile.ClassName;

/**
 * This contains the state of a single class.
 *
 * @since 2020/12/16
 */
final class __ClassState__
{
	/** The class data. */
	public final MinimizedClassFile classFile;
	
	/** The name of this class. */
	public final ClassName thisName;
	
	/**
	 * Initializes the base empty class state.
	 * 
	 * @param __thisName The name of this class.
	 * @param __classFile The class file data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	__ClassState__(ClassName __thisName, MinimizedClassFile __classFile)
		throws NullPointerException
	{
		if (__thisName == null || __classFile == null)
			throw new NullPointerException("NARG");
		
		this.thisName = __thisName;
		this.classFile = __classFile;
	}
}
