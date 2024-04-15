// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.ClassName;

/**
 * Linkage for class objects.
 *
 * @since 2023/07/16
 */
public class ClassObjectLinkage
	implements Linkage
{
	/** The class name. */
	protected final ClassName className;
	
	public ClassObjectLinkage(ClassName __className)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		this.className = __className;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof ClassObjectLinkage))
			return false;
		
		return this.className.equals(((ClassObjectLinkage)__o).className);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public int hashCode()
	{
		return this.className.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public void write(CStructVariableBlock __output)
		throws IOException, NullPointerException
	{
		try (CStructVariableBlock struct =
			 __output.memberStructSet("classObject"))
		{
			struct.memberSet("className",
				CBasicExpression.string(this.className.toString()));
		}
	}
}
