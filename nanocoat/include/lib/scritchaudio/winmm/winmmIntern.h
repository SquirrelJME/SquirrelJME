/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Windows MultiMedia internals.
 * 
 * @since 2025/06/03
 */

#ifndef SJME_C_WINMMINTERN_H
#define SJME_C_WINMMINTERN_H

#include "lib/scritchaudio/winmm/winmm.h"

#define WIN32_LEAN_AND_MEAN 1

#include <windows.h>
#include <mmsystem.h>

#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_XP)
	#include <mmreg.h>
#endif

#undef WIN32_LEAN_AND_MEAN

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_WINMMINTERN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_scritchaudio_winmm_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState);

sjme_errorCode sjme_scritchaudio_winmm_disconnect(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConn);

sjme_errorCode sjme_scritchaudio_winmm_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo);

sjme_errorCode sjme_scritchaudio_winmm_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list(sjme_scritchaudio_midiPort)* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);

sjme_errorCode sjme_scritchaudio_winmm_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);

sjme_errorCode sjme_scritchaudio_winmm_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_WINMMINTERN_H
}
#undef SJME_CXX_WINMMINTERN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_WINMMINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_WINMMINTERN_H */
