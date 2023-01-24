// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io.file;


import cc.squirreljme.runtime.cldc.annotation.Api;

public interface FileSystemListener
{
	@Api
	int ROOT_ADDED =
		0;
	
	@Api
	int ROOT_REMOVED =
		1;
	
	@Api
	void rootChanged(int __a, String __b);
}


