// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;

public interface ParameterizedType
	extends Type
{
	public abstract ClassDoc asClassDoc();
	
	public abstract Type containingType();
	
	public abstract Type[] interfaceTypes();
	
	public abstract Type superclassType();
	
	public abstract Type[] typeArguments();
}


