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


