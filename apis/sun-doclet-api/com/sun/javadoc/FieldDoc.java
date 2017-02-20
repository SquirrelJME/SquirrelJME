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


