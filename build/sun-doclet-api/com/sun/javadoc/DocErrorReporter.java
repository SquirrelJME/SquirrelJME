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

public interface DocErrorReporter
{
	public abstract void printError(String __a);
	
	public abstract void printError(SourcePosition __a, String __b);
	
	public abstract void printNotice(String __a);
	
	public abstract void printNotice(SourcePosition __a, String __b);
	
	public abstract void printWarning(String __a);
	
	public abstract void printWarning(SourcePosition __a, String __b);
}


