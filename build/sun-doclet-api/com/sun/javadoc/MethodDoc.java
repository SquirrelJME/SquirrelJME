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

public interface MethodDoc
	extends ExecutableMemberDoc
{
	public abstract boolean isAbstract();
	
	public abstract boolean isDefault();
	
	public abstract ClassDoc overriddenClass();
	
	public abstract MethodDoc overriddenMethod();
	
	public abstract Type overriddenType();
	
	public abstract boolean overrides(MethodDoc __a);
	
	public abstract Type returnType();
}


