// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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


