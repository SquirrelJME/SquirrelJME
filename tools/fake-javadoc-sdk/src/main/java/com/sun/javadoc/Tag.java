// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
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


