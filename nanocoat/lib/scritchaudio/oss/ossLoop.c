/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <unistd.h>
#include <sys/ioctl.h>

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/oss/ossIntern.h"

sjme_errorCode sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error;
	int fd, trigger;
	sjme_pointer buf;
	sjme_jint bufSize, i, n;
	sjme_scritchaudio_source source;
	sjme_list_sjme_scritchaudio_source* sources;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover stream, ignore if not ready yet. */
	inStream = inState->stream;
	if (inStream == NULL)
		return SJME_ERROR_NONE;
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	sjme_message("OSS Tick: %lld", inState->clock.clock.full / 1000000);
#endif

	/* Recover the single source. */
	sources = inStream->sources;
	if (sources == NULL)
		return SJME_ERROR_NONE;
	source = NULL;
	for (i = 0, n = sources->length; i < n; i++)
	{
		source = sources->elements[i];
		if (source != NULL)
			break;
	}

	/* None found? */
	if (source == NULL)
		return SJME_ERROR_NONE;

	/* Recover the file descriptor. */
	fd = inStream->data.fd;
	
	/* Allocate sample buffer */
	bufSize = renderInfo->bufSize;
	buf = sjme_alloca(bufSize);
	if (buf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(buf, 0, bufSize);

	/* Render source. */
	if (sjme_error_is(error = source->renderFunc(inState,
		source, renderInfo, (sjme_scritchaudio_buffer*)buf)))
		return sjme_error_default(error);
	
	/* Disable playback. */
	trigger = 0;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);

	/* Write the buffer data. */
	write(fd, buf, bufSize);

	/* Resume playback. */
	trigger = PCM_ENABLE_OUTPUT;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);
	ioctl(fd, SNDCTL_DSP_POST, NULL);

	/* Nothing. */
	return SJME_ERROR_NONE;
}
