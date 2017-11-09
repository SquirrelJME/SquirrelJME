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


