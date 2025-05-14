/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal ScritchAudio definitions.
 * 
 * @since 2025/05/10
 */

#ifndef SJME_C_SCRITCHAUDIOINTERN_H
#define SJME_C_SCRITCHAUDIOINTERN_H

#include "lib/scritchaudio/scritchaudio.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SCRITCHAUDIOINTERN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
sjme_errorCode sjme_scritchaudio_core_destroy(
	sjme_attrInNotNull sjme_scritchaudio inState);

sjme_errorCode sjme_scritchaudio_core_init(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInNotNull const sjme_scritchaudio_implFunctions* inImplFunc);
	
sjme_errorCode sjme_scritchaudio_core_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list_sjme_scritchaudio_midiPort* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);
	
sjme_errorCode sjme_scritchaudio_core_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source);
	
sjme_errorCode sjme_scritchaudio_core_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SCRITCHAUDIOINTERN_H
}
#undef SJME_CXX_SCRITCHAUDIOINTERN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SCRITCHAUDIOINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SCRITCHAUDIOINTERN_H */
