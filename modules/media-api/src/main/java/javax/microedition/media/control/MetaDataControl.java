// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface MetaDataControl
	extends Control
{
	String AUTHOR_KEY =
		"author";
	
	String COPYRIGHT_KEY =
		"copyright";
	
	String DATE_KEY =
		"date";
	
	String TITLE_KEY =
		"title";
	
	String getKeyValue(String __a);
	
	String[] getKeys();
}


