/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/* //// MLE /// */
#define mleGroupId TypeShelf
#define mleShelfClass "cc/squirreljme/jvm/mle/TypeShelf"
#define mleProxyTarget "cc/squirreljme/emulator/EmulatedTypeShelf"
#include "squirreljmeMle.h"
/* //////////// */

#include "squirreljme.h"

#define MLE_DESC_binaryName DESC_METHOD(DESC_STRING, DESC_TYPE)
MLE_FUNC_PROXY_STATIC(jobject, binaryName)

#define MLE_DESC_componentRoot DESC_METHOD(DESC_TYPE, DESC_TYPE)
MLE_FUNC_PROXY_STATIC(jobject, componentRoot)

#define MLE_DESC_findType DESC_METHOD(DESC_TYPE, DESC_STRING)
MLE_FUNC_PROXY_STATIC(jobject, findType)

#define MLE_DESC_inJar DESC_METHOD(DESC_JARPACKAGE, DESC_TYPE)
MLE_FUNC_PROXY_STATIC(jobject, inJar)

#define MLE_DESC_interfaces DESC_METHOD(DESC_ARRAY(DESC_TYPE), DESC_TYPE)
MLE_FUNC_PROXY_STATIC(jobject, interfaces)

#define MLE_DESC_isArray DESC_METHOD(DESC_BOOLEAN, DESC_TYPE)
MLE_FUNC_PROXY_STATIC(jboolean, isArray)

MLE_LIST_BEGIN()
	MLE_LIST_ITEM(binaryName),
	MLE_LIST_ITEM(componentRoot),
	MLE_LIST_ITEM(findType),
	MLE_LIST_ITEM(inJar),
	MLE_LIST_ITEM(interfaces),
	MLE_LIST_ITEM(isArray),
MLE_LIST_END()
