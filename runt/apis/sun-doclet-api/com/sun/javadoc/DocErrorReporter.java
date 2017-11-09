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

public interface DocErrorReporter
{
	public abstract void printError(String __a);
	
	public abstract void printError(SourcePosition __a, String __b);
	
	public abstract void printNotice(String __a);
	
	public abstract void printNotice(SourcePosition __a, String __b);
	
	public abstract void printWarning(String __a);
	
	public abstract void printWarning(SourcePosition __a, String __b);
}


