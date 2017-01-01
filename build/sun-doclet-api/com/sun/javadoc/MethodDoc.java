// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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


