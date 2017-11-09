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

public interface SerialFieldTag
	extends Tag, Comparable<Object>
{
	public abstract int compareTo(Object __a);
	
	public abstract String description();
	
	public abstract String fieldName();
	
	public abstract String fieldType();
	
	public abstract ClassDoc fieldTypeDoc();
}


