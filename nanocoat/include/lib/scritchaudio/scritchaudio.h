/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Main ScritchAudio header.
 * 
 * @since 2025/05/07
 */

#ifndef SJME_C_SCRITCHAUDIO_H
#define SJME_C_SCRITCHAUDIO_H

#include "sjme/config.h"
#include "sjme/alloc.h"
#include "sjme/list.h"
#include "lib/scritchany/scritchany.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SCRITCHAUDIO_H
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

/** A list of audio streams. */
SJME_LIST_DECLARE(sjme_scritchaudio_stream, 0);

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

/** A list of sources. */
SJME_LIST_DECLARE(sjme_scritchaudio_source, 0);

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

/** A list of ScritchAudio MIDI ports. */
SJME_LIST_DECLARE(sjme_scritchaudio_midiPort, 0);

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

/** A list of connections. */
SJME_LIST_DECLARE(sjme_scritchaudio_connection, 0);

/** Cast to connection type. */
#define SJME_AS_AUDIO_CONN(x) ((sjme_scritchaudio_connection)(x))

/**
 * The audio format for audio encoding.
 *
 * @since 2025/05/08
 */
typedef enum sjme_scritchaudio_format
{
	/** Automatic. */
	SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC = -1,
	
	/** Unsigned 8-bit PCM. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8 = 0,
	
	/** 8-bit a-law. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_ALAW = 1,
	
	/** 8-bit mu-law. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_ULAW = 2,
	
	/** Signed 16-bit. */
	SJME_SCRITCHAUDIO_FORMAT_SHORT_S16 = 3,
	
	/** Signed 24-bit, as integer type. */
	SJME_SCRITCHAUDIO_FORMAT_INT_S24 = 4,
	
	/** Signed 32-bit. */
	SJME_SCRITCHAUDIO_FORMAT_INT_S32 = 5,
	
	/** 32-bit floating point. */
	SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32 = 6,
	
	/** The number of audio formats. */
	SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS = 7,
} sjme_scritchaudio_format;

/**
 * The standard set of audio sampling rates.
 *
 * @since 2025/05/08
 */
typedef enum sjme_scritchaudio_rate
{
	/** Automatic. */
	SJME_SCRITCHAUDIO_RATE_AUTOMATIC = -1,
	
	/** 8000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_8000 = 8000,
	
	/** 11025 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_11025 = 11025,
	
	/** 16000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_16000 = 16000,
	
	/** 22050 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_22050 = 22050,
	
	/** 24000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_24000 = 24000,
	
	/** 44100 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_44100 = 44100,
	
	/** 48000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_48000 = 48000,
	
	/** Maximum supported sample rate. */
	SJME_SCRITCHAUDIO_RATE_MAX_SAMPLE_RATE = 384000,
} sjme_scritchaudio_rate;

/**
 * The output channels.
 *
 * @since 2025/05/08
 */
typedef enum sjme_scritchaudio_channels
{
	/** Automatic. */
	SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC = -1,
	
	/** Mono audio. */
	SJME_SCRITCHAUDIO_CHANNELS_MONO = 1,
	
	/** Stereo. */
	SJME_SCRITCHAUDIO_CHANNELS_STEREO = 2,
	
	/** Basic surround sound. */
	SJME_SCRITCHAUDIO_CHANNELS_BASIC_SURROUND = 4,
	
	/** Full surround sound. */
	SJME_SCRITCHAUDIO_CHANNELS_FULL_SURROUND = 8,
} sjme_scritchaudio_channels;

/**
 * Internal implementation initialization function.
 *
 * @param inState The ScritchAudio state.
 * @return Any resultant error, if any.
 * @since 2025/05/12
 */
typedef sjme_errorCode (*sjme_scritchaudio_apiInitFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState);

/**
 * Disconnects the given connection.
 *
 * @param inState The ScritchAudio state.
 * @param inConnection The connection being disconnected.
 * @return Any resultant error, if any.
 * @since 2025/05/26
 */
typedef sjme_errorCode (*sjme_scritchaudio_disconnectFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConnection);

/**
 * Called when the peer has been disconnected.
 *
 * @param inState The ScritchAudio state.
 * @param inConnection The connection being disconnected.
 * @param inPeer The peer that disconnected.
 * @return Any resultant error, if any.
 * @since 2025/05/26
 */
typedef sjme_errorCode (*sjme_scritchaudio_disconnectPeerFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_connection inConnection,
	sjme_attrInNotNull sjme_scritchaudio_connection inPeer);
	
/**
 * Loop iteration for audio processing, if there is no background thread
 * for audio-processing.
 *
 * @param inState The ScritchAudio state.
 * @return Any resultant error, if any.
 * @since 2025/05/15
 */
typedef sjme_errorCode (*sjme_scritchaudio_loopIterateFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState);
	
/**
 * Queries the MIDI ports and synths that are available to the system.
 *
 * @param inState The ScritchAudio state.
 * @param inOutPorts The ports which are available.
 * @param outNumPorts The number of ports that are available, this value may
 * be larger than the list if it is too small.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_queryMidiPortsFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list_sjme_scritchaudio_midiPort* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);

/**
 * Callback function for when a render is occurring.
 *
 * @param inState The ScritchAudio state.
 * @param inSource The source being rendered.
 * @return Any resultant error, if any.
 * @since 2025/05/18
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceRenderFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);
	
/**
 * Attaches a source renderer to the given stream, the renderer will use the
 * same format that the stream uses.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to attach to or detach from.
 * @param outSource The resultant source.
 * @param renderFunc The render function to use.
 * @param initFrontEnd The front end used for the renderer.
 * @return Any resultant error, if any.
 * @since 2025/05/18
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceAttachFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrOutNullable sjme_scritchaudio_source* outSource,
	sjme_attrInNotNull sjme_scritchaudio_sourceRenderFunc renderFunc,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd);

/**
 * Attaches a source renderer to the given stream, the renderer will use the
 * same format that the stream uses.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to attach to or detach from.
 * @param inSource The resultant source.
 * @return Any resultant error, if any.
 * @since 2025/05/25
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceAttachImplFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_source inSource);

/**
 * Creates a new audio stream.
 *
 * @param inState The input state.
 * @param outStream The resultant audio stream.
 * @param inName The name of the stream.
 * @param inFormat The audio format to use, @c -1 means to use the system
 * preferred format.
 * @param inRate The rate to use, @c -1 means to use the system preferred
 * rate.
 * @param inChannels The number of channels to use, @c -1 means to use the
 * system preferred channels.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_streamCreateFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);
	
/**
 * Functions for operating on ScritchAudio.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudio_apiFunctions
{
	/** Disconnects a connection. */
	sjme_scritchaudio_disconnectFunc disconnect;
	
	/** Iterates the audio loop. */
	sjme_scritchaudio_loopIterateFunc loopIterate;
	
	/** Queries the MIDI ports and synths available. */
	sjme_scritchaudio_queryMidiPortsFunc queryMidiPorts;
	
	/** Attaches or detaches a source. */
	sjme_scritchaudio_sourceAttachFunc sourceAttach;
	
	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateFunc streamCreate;
} sjme_scritchaudio_apiFunctions;

/**
 * ScritchAudio implementation functions.
 *
 * @since 2025/05/10
 */
typedef struct sjme_scritchaudio_implFunctions
{
	/** Api initialization. */
	sjme_scritchaudio_apiInitFunc apiInit;
	
	/** Disconnects a connection. */
	sjme_scritchaudio_disconnectFunc disconnect;
	
	/** Iterates the audio loop. */
	sjme_scritchaudio_loopIterateFunc loopIterate;
	
	/** Queries the MIDI ports and synths available. */
	sjme_scritchaudio_queryMidiPortsFunc queryMidiPorts;
	
	/** Attaches or detaches a source. */
	sjme_scritchaudio_sourceAttachImplFunc sourceAttach;
	
	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateFunc streamCreate;
} sjme_scritchaudio_implFunctions;

/**
 * ScritchAudio bugs.
 *
 * @since 2025/05/15
 */
typedef struct sjme_scritchaudio_bugs
{
	/** Audio is manually polled, there is no system managed loop. */
	sjme_jboolean manualPoll;
} sjme_scritchaudio_bugs;

/**
 * ScritchAudio time.
 *
 * @since 2025/05/16
 */
typedef struct sjme_scritchaudio_time
{
	/** Milliseconds. */
	sjme_jint millis;

	/** Nanoseconds. */
	sjme_jint nanos;
} sjme_scritchaudio_time;

/** The sleeping rate when no audio is playing (millis). */
#define SJME_SCRITCHAUDIO_SLEEP_RATE_MS 1000

/** The sleeping rate when no audio is playing (nanos). */
#define SJME_SCRITCHAUDIO_SLEEP_RATE_NS 0

struct sjme_scritchaudioBase
{
	/** The lock for audio streams and otherwise. */
	sjme_thread_spinLock lock;
	
	/** The allocation pool to use. */
	sjme_alloc_pool pool;

	/** The front end. */
	sjme_frontEnd frontEnd;
	
	/** Api Functions. */
	const sjme_scritchaudio_apiFunctions* api;

	/** Implementation functions. */
	const sjme_scritchaudio_implFunctions* impl;
	
	/** The audio loop thread, if applicable. */
	sjme_thread loopThread;
	
	/** The current audio thread ID, if applicable. */
	sjme_thread_id loopThreadId;

	/** The loop thread is ready. */
	sjme_atomic_sjme_jint loopThreadReady;
	
	/** Wrapped ScritchAudio state, if this is a wrapper. */
	sjme_scritchaudio wrappedState;
	
	/** Reference to the owning state. */
	sjme_alignPointer sjme_atomic_sjme_pointer topState;

	/** Bugs. */
	sjme_scritchaudio_bugs bugs;

	/** The delay between manual polls (Millis). */
	sjme_atomic_sjme_jint pollDelayMillis;

	/** The delay between manual polls (Nanos). */
	sjme_atomic_sjme_jint pollDelayNanos;

	/** The currently active audio streams. */
	sjme_list_sjme_scritchaudio_stream* streams;
};

/**
 * Represents the type that a connection is.
 *
 * @since 2025/05/26
 */
typedef enum sjme_scritchaudio_connectionType
{
	/** Invalid connection type. */
	SJME_SCRITCHAUDIO_CONN_INVALID,

	/** A stream. */
	SJME_SCRITCHAUDIO_CONN_STREAM,

	/** A source which generates data for a stream. */
	SJME_SCRITCHAUDIO_CONN_SOURCE,

	/** A sink which reads data from a device, such as a microphone. */
	SJME_SCRITCHAUDIO_CONN_SINK,
	
	/** The number of connection types. */
	SJME_SCRITCHAUDIO_NUM_CONN_TYPES
} sjme_scritchaudio_connectionType;

struct sjme_scritchaudio_connectionBase
{
	/** The lock for this source, used when rendering. */
	sjme_thread_spinLock lock;

	/** The type of connection this is. */
	sjme_scritchaudio_connectionType type;
	
	/** The state this is in. */
	sjme_scritchaudio inState;

	/** Internal handler for handling disconnection. */
	sjme_scritchaudio_disconnectFunc handleDisconnect;

	/** Called when the peer disconnected. */
	sjme_scritchaudio_disconnectPeerFunc peerDisconnected;

	/** The connections this is connected to. */
	sjme_list_sjme_scritchaudio_connection* peers;
};

struct sjme_scritchaudio_streamBase
{
	/** The connection. */
	sjme_scritchaudio_connectionBase connection;

	/** The stream format. */
	sjme_scritchaudio_format format;

	/** The stream rate. */
	sjme_scritchaudio_rate rate;

	/** The stream channels. */
	sjme_scritchaudio_channels channels;

	/** The sources attached to this stream. */
	sjme_list_sjme_scritchaudio_source* sources;

	/** Stream data. */
	struct
	{
		/** The file descriptor, if applicable. */
		int fd;
		
		/** The stream this wrapped. */
		sjme_scritchaudio_stream wrapped;
	} data;
};

struct sjme_scritchaudio_sourceBase
{
	/** The connection. */
	sjme_scritchaudio_connectionBase connection;

	/** The stream this is attached to. */
	sjme_scritchaudio_stream inStream;

	/** The renderer function. */
	sjme_scritchaudio_sourceRenderFunc renderFunc;

	/** The front end data. */
	sjme_frontEnd frontEnd;

	/** The source data. */
	struct
	{
		/** The wrapped source. */
		sjme_scritchaudio_source wrapped;
	} data;
};
	
/**
 * Dynamic library entry point for ScritchAudio.
 *
 * @param inPool The input pool.
 * @param outState The resultant audio state.
 * @param initFrontEnd the initial front-end state.
 * @return Any resultant error, if any.
 * @since 2025/05/11
 */
typedef sjme_errorCode (sjme_attrExportCall *sjme_scritchaudio_dylibApiFunc)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchaudio* outState,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd);
	
/** The base name for the ScritchAudio dynamic library. */
#define SJME_SCRITCHAUDIO_DYLIB_NAME_BASE \
	SJME_SCRITCHANY_DYLIB_NAME_BASE(audio)

/** The name of the dynamic library for ScritchAudio. */
#define SJME_SCRITCHAUDIO_DYLIB_NAME(x) \
	SJME_SCRITCHANY_DYLIB_NAME(audio, x)

/** The path name for the dynamic library for ScritchAudio. */
#define SJME_SCRITCHAUDIO_DYLIB_PATHNAME(x) \
	SJME_SCRITCHANY_DYLIB_PATHNAME(audio, x)

/** The prefix for the dynamic library. */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL_PREFIX \
	SJME_SCRITCHANY_DYLIB_SYMBOL_PREFIX(audio)

/** The symbol to use with @c sjme_scritchaudio_dylibApiFunc . */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL(audio, x)

/** Declares the API export . */
#define SJME_SCRITCHAUDIO_DYLIB_SYMBOL_DECLARE(x) \
	SJME_SCRITCHANY_DYLIB_SYMBOL_DECLARE(audio, x)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SCRITCHAUDIO_H
}
#undef SJME_CXX_SCRITCHAUDIO_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SCRITCHAUDIO_H */
#endif /* #ifdef __cplusplus */

#endif /* SCRITCHAUDIO_H */
