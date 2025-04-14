// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

/**
 * This represents the action to take when working with a decoder.
 *
 * @since 2014/08/04
 */
public enum BaseDecoderKind
{
	/** Push an object. */
	PUSH_OBJECT,
	
	/** Push an array. */
	PUSH_ARRAY,
	
	/** Declare key. */
	DECLARE_KEY,
	
	/** Sets the value of the key in an object. */
	ADD_OBJECT_KEYVAL,
	
	/** Add array value. */
	ADD_ARRAY_VALUE,
	
	/** Pop an array, then add to the value of a key. */
	POP_ARRAY_ADD_OBJECT_KEYVAL,
	
	/** Pop an array, then add to an array. */
	POP_ARRAY_ADD_ARRAY,
	
	/** Pop an object, then add to the array. */
	POP_OBJECT_ADD_ARRAY,
	
	/** Pop object and add to object as value of an object. */
	POP_OBJECT_ADD_OBJECT_KEYVAL,
	
	/** Finished object. */
	FINISHED_OBJECT,
	
	/** Finished array. */
	FINISHED_ARRAY,
	
	/** End. */
	;
}
