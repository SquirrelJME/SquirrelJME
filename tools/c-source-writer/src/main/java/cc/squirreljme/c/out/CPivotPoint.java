// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

/**
 * Indicates a pivot point within the C code, this is mostly just used for
 * formatting purposes. It indicates when something is about to be declared
 * and potentially finished declared.
 *
 * @since 2023/07/21
 */
public enum CPivotPoint
{
	/** Start of statement. */
	STATEMENT_START,
	
	/** End of statement. */
	STATEMENT_END,
	
	/** Declare something inside a function. */
	DECLARE_INSIDE,
	
	/** Function parameter. */
	FUNCTION_PARAMETER,
	
	/** Label for use with goto. */
	LABEL_GOTO,
	
	/** Switch continue or break. */
	SWITCH_CONTINUE_OR_BREAK,
	
	/** Operator, left side. */
	OPERATOR_LEFT,
	
	/** Operator, right side. */
	OPERATOR_RIGHT,
	
	/** Set of struct member. */
	STRUCT_MEMBER_SET,
	
	/** Set of array member. */
	ARRAY_MEMBER_SET,
	
	/** Line comment. */
	LINE_COMMENT,
	
	/** Preprocessor statement. */
	PREPROCESSOR_STATEMENT,
	
	/* End. */
	;
}
