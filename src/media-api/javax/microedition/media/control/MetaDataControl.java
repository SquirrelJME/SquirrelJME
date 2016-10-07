// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface MetaDataControl
	extends Control
{
	public static final String AUTHOR_KEY =
		"author";
	
	public static final String COPYRIGHT_KEY =
		"copyright";
	
	public static final String DATE_KEY =
		"date";
	
	public static final String TITLE_KEY =
		"title";
	
	public abstract String getKeyValue(String __a);
	
	public abstract String[] getKeys();
}


