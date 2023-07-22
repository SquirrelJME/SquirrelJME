// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a last-in-first-out, this uses {@link Vector} as a base and provides
 * operations for pushing and popping.
 *
 * It is recommended to use {@link LinkedList} or {@link ArrayDeque} as they
 * are newer classes.
 *
 * @since 2019/05/08
 */
@Api
public class Stack<E>
	extends Vector<E>
{
	@Api
	public boolean empty()
	{
		throw Debugging.todo();
	}
	
	@Api
	public E peek()
	{
		throw Debugging.todo();
	}
	
	@Api
	public E pop()
	{
		throw Debugging.todo();
	}
	
	@Api
	public E push(E __item)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int search(Object __o)
	{
		throw Debugging.todo();
	}
}

