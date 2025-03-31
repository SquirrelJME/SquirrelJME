// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;

public interface ThrowsTag
	extends Tag
{
	public abstract ClassDoc exception();
	
	public abstract String exceptionComment();
	
	public abstract String exceptionName();
	

	public abstract Type exceptionType();
}


