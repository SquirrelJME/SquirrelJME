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


