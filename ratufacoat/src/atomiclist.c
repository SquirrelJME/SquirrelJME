/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "atomic.h"
#include "atomiclist.h"
#include "sjmerc.h"

/**
 * This represents a tread within an atomic list which may change accordingly
 * when the size of the list changes.
 * 
 * @since 2022/03/27
 */
typedef struct sjme_atomicListTread
{
	/** The length of this tread. */
	sjme_jint length;
	
	/** Values within this tread. */
	sjme_atomicPointer values[0];
} sjme_atomicListTread;

struct sjme_atomicList
{
	sjme_atomicListTread tread;
};
