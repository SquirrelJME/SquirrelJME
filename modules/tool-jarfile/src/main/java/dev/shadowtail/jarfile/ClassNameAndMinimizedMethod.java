// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import dev.shadowtail.classfile.mini.MinimizedMethod;
import net.multiphasicapps.classfile.ClassName;

/**
 * This contains a class and a minimized method.
 *
 * @since 2019/09/21
 */
public final class ClassNameAndMinimizedMethod
{
	/** The class name. */
	public final ClassName classname;
	
	/** The method. */
	public final MinimizedMethod method;
	
	/**
	 * Initializes the class name and method.
	 *
	 * @param __cn The class name.
	 * @param __mm The minimized method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/21
	 */
	public ClassNameAndMinimizedMethod(ClassName __cn, MinimizedMethod __mm)
		throws NullPointerException
	{
		if (__cn == null || __mm == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
		this.method = __mm;
	}
}
