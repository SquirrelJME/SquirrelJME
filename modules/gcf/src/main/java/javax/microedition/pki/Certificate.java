// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.pki;


import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface Certificate
{
	@Api
	String getIssuer();
	
	@Api
	long getNotAfter();
	
	@Api
	long getNotBefore();
	
	@Api
	String getSerialNumber();
	
	@Api
	String getSigAlgName();
	
	@Api
	String getSubject();
	
	@Api
	String getType();
	
	@Api
	String getVersion();
}


