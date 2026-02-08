/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal OSS definitions.
 * 
 * @since 2025/05/07
 */

#ifndef SJME_C_OSSINTERN_H
#define SJME_C_OSSINTERN_H

#include "lib/scritchaudio/oss/oss.h"

#if defined(SQUIRRELJME_OSS_INCLUDE_FILE)
	#include SQUIRRELJME_OSS_INCLUDE_FILE
#else
	#include <sys/soundcard.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_OSSINTERN_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The device name for digital audio output. */
#define SJME_SCRITCHAUDIO_OSS_DSP "/dev/dsp"

/** The device name for MIDI input/output. */
#define SJME_SCRITCHAUDIO_OSS_MIDI "/dev/midi"
	
sjme_errorCode sjme_scritchaudio_oss_apiInit(
	sjme_attrInNotNull sjme_scritchaudio inState);

sjme_errorCode sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream);
	
sjme_errorCode sjme_scritchaudio_oss_queryMidiPorts(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list(sjme_scritchaudio_midiPort)* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);
	
sjme_errorCode sjme_scritchaudio_oss_sourceAttach(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);
	
sjme_errorCode sjme_scritchaudio_oss_streamCreate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_scritchaudio_stream inOutStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_OSSINTERN_H
}
#undef SJME_CXX_OSSINTERN_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_OSSINTERN_H */
#endif /* #ifdef __cplusplus */

#endif /* OSSINTERN_H */
