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

public interface AnnotationDesc
{
	public abstract AnnotationTypeDoc annotationType();
	
	public abstract AnnotationDesc.ElementValuePair[] elementValues();
	
	public abstract boolean isSynthesized();
	
	public static interface ElementValuePair
	{
		public abstract AnnotationTypeElementDoc element();
		
		public abstract AnnotationValue value();
	}
}


