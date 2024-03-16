// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;


public interface FieldDoc
	extends MemberDoc
{

	public abstract Object constantValue();
	

	public abstract String constantValueExpression();
	

	public abstract boolean isTransient();
	

	public abstract boolean isVolatile();
	

	public abstract SerialFieldTag[] serialFieldTags();
	

	public abstract Type type();
}


