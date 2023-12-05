// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * This represents a directive within the C preprocessor.
 *
 * @since 2023/06/04
 */
public enum CPPDirective
{
	/** Define macro. */
	DEFINE("define"),
	
	/** Else if. */
	ELIF("elif"),
	
	/** Else. */
	ELSE("else"),
	
	/** End if. */
	ENDIF("endif"),
	
	/** Check something. */
	IF("if"),
	
	/** Include a file. */
	INCLUDE("include"),
	
	/** Undefine a macro. */
	UNDEF("undef"),
	
	/* End. */
	;
	
	/** The directive used. */
	public final String directive;
	
	/**
	 * Initializes the CPP Directive.
	 * 
	 * @param __directive The directive to define.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	CPPDirective(String __directive)
		throws NullPointerException
	{
		if (__directive == null)
			throw new NullPointerException("NARG");
		
		this.directive = __directive;
	}
}
