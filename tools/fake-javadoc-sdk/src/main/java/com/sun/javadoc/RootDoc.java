// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
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


