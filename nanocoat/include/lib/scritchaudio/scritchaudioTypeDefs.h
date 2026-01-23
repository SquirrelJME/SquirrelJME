/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Typedefs for ScritchAudio.
 *
 * @file
 * @since 2026/01/23
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H
#define SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H

#include "sjme/list.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * ScritchAudio state structure.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudioBase sjme_scritchaudioBase;

/**
 * ScritchAudio state structure.
 *
 * @since 2025/05/07
 */
typedef sjme_scritchaudioBase* sjme_scritchaudio;

/**
 * A single ScritchAudio stream.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_streamBase sjme_scritchaudio_streamBase;

/**
 * A single ScritchAudio stream.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_streamBase* sjme_scritchaudio_stream;

/**
 * A ScritchAudio render source.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_sourceBase sjme_scritchaudio_sourceBase;

/**
 * A ScritchAudio render source.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_sourceBase* sjme_scritchaudio_source;

/**
 * ScritchAudio latency information.
 *
 * @since 2026/01/23
 */
typedef struct sjme_scritchaudio_latency sjme_scritchaudio_latency;

/**
 * A single ScritchAudio MIDI port.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_midiPortBase sjme_scritchaudio_midiPortBase;

/**
 * A single ScritchAudio MIDI port.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_midiPortBase* sjme_scritchaudio_midiPort;

/**
 * Represents a single connection.
 *
 * @since 2025/05/26
 */
typedef struct sjme_scritchaudio_connectionBase
	sjme_scritchaudio_connectionBase;

/**
 * Represents a single connection.
 *
 * @since 2025/05/26
 */
typedef sjme_scritchaudio_connectionBase* sjme_scritchaudio_connection;

/**
 * This contains the information needed to render.
 *
 * @since 2025/05/31
 */
typedef struct sjme_scritchaudio_renderInfo sjme_scritchaudio_renderInfo;

/**
 * The rendering format used.
 *
 * @since 2026/01/23
 */
typedef struct sjme_scritchaudio_renderFormat sjme_scritchaudio_renderFormat;

/**
 * An individual stream buffer.
 *
 * @since 2026/01/20
 */
typedef struct sjme_scritchaudio_streamBuffer sjme_scritchaudio_streamBuffer;

/**
 * The data associated with a stream.
 *
 * @since 2026/01/20
 */
typedef struct sjme_scritchaudio_streamData sjme_scritchaudio_streamData;

/** A list of audio streams. */
SJME_LIST_DECLARE(sjme_scritchaudio_stream, 0);

/** A list of sources. */
SJME_LIST_DECLARE(sjme_scritchaudio_source, 0);

/** A list of ScritchAudio MIDI ports. */
SJME_LIST_DECLARE(sjme_scritchaudio_midiPort, 0);

/** A list of connections. */
SJME_LIST_DECLARE(sjme_scritchaudio_connection, 0);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPEDEFS_H */