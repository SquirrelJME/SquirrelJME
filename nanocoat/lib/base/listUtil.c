/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/listUtil.h"

sjme_errorCode sjme_listUtil_intStringsFromBuffer(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNullBuf(length) sjme_lpcstr buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_listUtil_intStringsFromStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_listUtil_stringsFromBuffer(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNullBuf(length) sjme_lpcstr buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_listUtil_stringsFromStream(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
