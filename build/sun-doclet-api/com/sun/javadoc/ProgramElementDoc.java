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

public interface ProgramElementDoc
	extends Doc
{
	public abstract AnnotationDesc[] annotations();
	
	public abstract ClassDoc containingClass();
	
	public abstract PackageDoc containingPackage();
	
	public abstract boolean isFinal();
	
	public abstract boolean isPackagePrivate();
	
	public abstract boolean isPrivate();
	
	public abstract boolean isProtected();
	
	public abstract boolean isPublic();
	
	public abstract boolean isStatic();
	
	public abstract int modifierSpecifier();
	
	public abstract String modifiers();
	
	public abstract String qualifiedName();
}


