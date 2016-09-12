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

public interface SerialFieldTag
	extends Tag, Comparable<Object>
{
	public abstract int compareTo(Object __a);
	
	public abstract String description();
	
	public abstract String fieldName();
	
	public abstract String fieldType();
	
	public abstract ClassDoc fieldTypeDoc();
}


