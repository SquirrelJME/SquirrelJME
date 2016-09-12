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

public interface Tag
{
	public abstract Tag[] firstSentenceTags();
	
	public abstract Doc holder();
	
	public abstract Tag[] inlineTags();
	
	public abstract String kind();
	
	public abstract String name();
	
	public abstract SourcePosition position();
	
	public abstract String text();
	
	@Override
	public abstract String toString();
}


