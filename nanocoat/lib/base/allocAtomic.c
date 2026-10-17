/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* ReSharper disable CppUnusedIncludeDirective */

#include "sjme/alloc.h"

#define sjme_atomic_type sjme_alloc_weak
#include "sjme/atomicTemplateC.h"

#define sjme_atomic_type sjme_alloc_link
#include "sjme/atomicTemplateC.h"
