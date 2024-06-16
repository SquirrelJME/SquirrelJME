/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/tree.h"
#include "sjme/debug.h"
#include "sjme/util.h"

sjme_jint sjme_tree_find(void* in, void* what,
	const sjme_tree_findFunc* functions)
{
	if (in == NULL || functions == NULL)
		return -1;
	
	sjme_todo("sjme_tree_find()");
	return -1;
}
