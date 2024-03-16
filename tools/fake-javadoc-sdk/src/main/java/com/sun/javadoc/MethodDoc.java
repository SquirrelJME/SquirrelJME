// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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


