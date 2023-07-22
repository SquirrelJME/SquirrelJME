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

@Api
public interface Queue<E>
	extends Collection<E>
{
	@Override
	boolean add(E __a);
	
	@Api
	E element();
	
	@Api
	boolean offer(E __a);
	
	@Api
	E peek();
	
	@Api
	E poll();
	
	@Api
	E remove();
}

