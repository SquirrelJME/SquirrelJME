// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import dev.shadowtail.classfile.mini.MinimizedField;
import net.multiphasicapps.classfile.ClassName;

/**
 * This contains a class and a minimized field.
 *
 * @since 2019/09/14
 */
@Deprecated
public final class ClassNameAndMinimizedField
{
	/** The class name. */
	@Deprecated
	public final ClassName classname;
	
	/** The field. */
	@Deprecated
	public final MinimizedField field;
	
	/**
	 * Initializes the class name and field.
	 *
	 * @param __cn The class name.
	 * @param __mf The minimized field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	@Deprecated
	public ClassNameAndMinimizedField(ClassName __cn, MinimizedField __mf)
		throws NullPointerException
	{
		if (__cn == null || __mf == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
		this.field = __mf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/21
	 */
	@Override
	@Deprecated
	public final String toString()
	{
		return this.classname + ":" + this.field.nameAndType();
	}
}

