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

public interface Type
{
	public abstract AnnotatedType asAnnotatedType();
	
	public abstract AnnotationTypeDoc asAnnotationTypeDoc();
	
	public abstract ClassDoc asClassDoc();
	
	public abstract ParameterizedType asParameterizedType();
	
	public abstract TypeVariable asTypeVariable();
	
	public abstract WildcardType asWildcardType();
	
	public abstract String dimension();
	
	public abstract Type getElementType();
	
	public abstract boolean isPrimitive();
	
	public abstract String qualifiedTypeName();
	
	public abstract String simpleTypeName();
	
	@Override
	public abstract String toString();
	
	public abstract String typeName();
}


