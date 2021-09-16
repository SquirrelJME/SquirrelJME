// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.pki;


public interface Certificate
{
	String getIssuer();
	
	long getNotAfter();
	
	long getNotBefore();
	
	String getSerialNumber();
	
	String getSigAlgName();
	
	String getSubject();
	
	String getType();
	
	String getVersion();
}


