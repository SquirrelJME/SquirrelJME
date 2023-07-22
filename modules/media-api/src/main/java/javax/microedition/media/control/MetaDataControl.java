// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;

@Api
public interface MetaDataControl
	extends Control
{
	@Api
	String AUTHOR_KEY =
		"author";
	
	@Api
	String COPYRIGHT_KEY =
		"copyright";
	
	@Api
	String DATE_KEY =
		"date";
	
	@Api
	String TITLE_KEY =
		"title";
	
	@Api
	String getKeyValue(String __a);
	
	@Api
	String[] getKeys();
}


