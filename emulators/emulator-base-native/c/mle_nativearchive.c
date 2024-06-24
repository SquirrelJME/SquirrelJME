/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

#define NAME_ARCHIVE "cc/squirreljme/jvm/mle/brackets/NativeArchiveBracket"
#define NAME_ENTRY "cc/squirreljme/jvm/mle/brackets/NativeArchiveEntryBracket"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/NativeArchiveShelf"
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedNativeArchiveShelf"

#define FORWARD_DESC_archiveClose "(" \
	DESC_CLASS(NAME_ARCHIVE) \
	")" DESC_VOID
#define FORWARD_DESC_archiveEntry "(" \
	DESC_CLASS(NAME_ARCHIVE) \
	DESC_STRING \
	")" DESC_CLASS(NAME_ENTRY)
#define FORWARD_DESC_archiveOpenZip "(" \
	DESC_ARRAY(DESC_BYTE) \
	DESC_INT \
	DESC_INT \
	")" DESC_CLASS(NAME_ARCHIVE)
#define FORWARD_DESC_entryIsDirectory "(" \
	DESC_CLASS(NAME_ENTRY) \
	")" DESC_BOOLEAN
#define FORWARD_DESC_entryOpen "(" \
	DESC_CLASS(NAME_ENTRY) \
	")" DESC_CLASS("java/io/InputStream")
#define FORWARD_DESC_entryUncompressedSize "(" \
	DESC_CLASS(NAME_ENTRY) \
	")" DESC_LONG
	
FORWARD_IMPL_VOID(NativeArchive, archiveClose, \
	FORWARD_IMPL_args(jobject archive), \
	FORWARD_IMPL_pass(archive))
FORWARD_IMPL(NativeArchive, archiveEntry, jobject, Object, \
	FORWARD_IMPL_args(jobject archive, jobject name), \
	FORWARD_IMPL_pass(archive, name))
FORWARD_IMPL(NativeArchive, archiveOpenZip, jobject, Object, \
	FORWARD_IMPL_args(jobject buf, jint off, jint len), \
	FORWARD_IMPL_pass(buf, off, len))
FORWARD_IMPL(NativeArchive, entryIsDirectory, jboolean, Boolean, \
	FORWARD_IMPL_args(jobject entry), \
	FORWARD_IMPL_pass(entry))
FORWARD_IMPL(NativeArchive, entryOpen, jobject, Object, \
	FORWARD_IMPL_args(jobject entry), \
	FORWARD_IMPL_pass(entry))
FORWARD_IMPL(NativeArchive, entryUncompressedSize, jlong, Long, \
	FORWARD_IMPL_args(jobject entry), \
	FORWARD_IMPL_pass(entry))

static const JNINativeMethod mleNativeArchiveMethods[] =
{
	FORWARD_list(NativeArchive, archiveClose),
    FORWARD_list(NativeArchive, archiveEntry),
    FORWARD_list(NativeArchive, archiveOpenZip),
    FORWARD_list(NativeArchive, entryIsDirectory),
    FORWARD_list(NativeArchive, entryOpen),
    FORWARD_list(NativeArchive, entryUncompressedSize)
};

FORWARD_init(mleNativeArchiveInit, mleNativeArchiveMethods)
