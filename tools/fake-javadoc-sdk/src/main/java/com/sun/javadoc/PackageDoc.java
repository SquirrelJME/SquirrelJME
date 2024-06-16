// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;


public interface PackageDoc
	extends Doc
{

	public abstract ClassDoc[] allClasses(boolean __a);
	

	public abstract ClassDoc[] allClasses();
	

	public abstract AnnotationTypeDoc[] annotationTypes();
	

	public abstract AnnotationDesc[] annotations();
	

	public abstract ClassDoc[] enums();
	

	public abstract ClassDoc[] errors();
	

	public abstract ClassDoc[] exceptions();
	

	public abstract ClassDoc findClass(String __a);
	

	public abstract ClassDoc[] interfaces();
	

	public abstract ClassDoc[] ordinaryClasses();
}


