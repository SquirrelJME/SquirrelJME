// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;

public interface SeeTag
	extends Tag
{
	public abstract String label();
	
	public abstract ClassDoc referencedClass();
	
	public abstract String referencedClassName();
	
	public abstract MemberDoc referencedMember();
	
	public abstract String referencedMemberName();
	
	public abstract PackageDoc referencedPackage();
}


