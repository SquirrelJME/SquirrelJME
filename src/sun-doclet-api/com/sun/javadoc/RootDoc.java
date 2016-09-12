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

public interface RootDoc
	extends Doc, DocErrorReporter
{
	public abstract ClassDoc classNamed(String __a);
	
	public abstract ClassDoc[] classes();
	
	public abstract String[][] options();
	
	public abstract PackageDoc packageNamed(String __a);
	
	public abstract ClassDoc[] specifiedClasses();
	
	public abstract PackageDoc[] specifiedPackages();
}


