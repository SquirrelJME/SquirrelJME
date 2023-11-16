/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/tread.h"

static const sjme_nvm_frameTreadAccessor sjme_nvm_constFrameTreadAccessor[
	SJME_NUM_JAVA_TYPE_IDS] =
{
	/* Integer. */
	{
		sizeof(jint),
		"jint",
		NULL,
		NULL
	},
	
	/* Long. */
	{
		sizeof(jlong),
		"jlong",
		NULL,
		NULL
	},
	
	/* Float. */
	{
		sizeof(jfloat),
		"jfloat",
		NULL,
		NULL
	},
	
	/* Double. */
	{
		sizeof(jdouble),
		"jdouble",
		NULL,
		NULL
	},
	
	/* Object. */
	{
		sizeof(jobject),
		"jobject",
		NULL,
		NULL
	}
};

const sjme_nvm_frameTreadAccessor* sjme_nvm_frameTreadAccessorByType(
	sjme_attrInRange(SJME_JAVA_TYPE_ID_INTEGER, SJME_NUM_JAVA_TYPE_IDS - 1)
		sjme_javaTypeId typeId)
{
	/* Not valid at all? */
	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return NULL;
	
	return &sjme_nvm_constFrameTreadAccessor[typeId];
}
