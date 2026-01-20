/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/
#include <unistd.h>

#if defined(SJME_CONFIG_HAS_SYS_IOCTL_H)
	#include <sys/ioctl.h>
#endif

#include "lib/scritchaudio/scritchaudioIntern.h"
#include "lib/scritchaudio/oss/ossIntern.h"

sjme_errorCode sjme_attrOptimize sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInNotNull sjme_scritchaudio_renderInfo* renderInfo)
{
	sjme_errorCode error, renderError;
	int fd, trigger;
	sjme_pointer buf;
	sjme_jint bufSize, i, n;
	sjme_scritchaudio_source source;
	sjme_list(sjme_scritchaudio_source)* sources;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover stream. */
	inStream = (inStream != NULL ? inStream : inState->stream);
	if (inStream == NULL)
		return SJME_ERROR_AUDIO_DESTROYED;

	/* Recover the file descriptor. */
	fd = inStream->data.fd;
	if (fd == -1)
		return SJME_ERROR_AUDIO_DESTROYED;

	/* Recover the single source. */
	sources = inStream->sources;
	if (sources == NULL)
		return SJME_ERROR_AUDIO_AWAITING;
	
	source = NULL;
	for (i = 0, n = sources->length; i < n; i++)
	{
		source = sources->elements[i];
		if (source != NULL)
			break;
	}

	/* None found? */
	if (source == NULL)
		return SJME_ERROR_AUDIO_AWAITING;
	
	/* Calculate the render info. */
	if (sjme_error_is(error = inState->intern->calcRenderInfo(
		inState, inStream, source, renderInfo)))
		return sjme_error_default(error);
	
	/* Allocate sample buffer */
	bufSize = renderInfo->bufSize;
	buf = sjme_alloca(bufSize);
	if (buf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Disable playback (if supported by the driver). */
	trigger = 0;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);
	
	/* If the source format is unsigned, we need to actually set the proper */
	/* zero level, otherwise there will be clicks/pops. */
	if (renderInfo->format == SJME_SCRITCHAUDIO_FORMAT_BYTE_U8)
		memset(buf, 0x80, bufSize);
	else
		memset(buf, 0, bufSize);

	/* Render source. */
	renderError = source->renderFunc(inState,
		source, renderInfo, (sjme_scritchaudio_buffer*)buf);

	/* Write the buffer data. */
	if (write(fd, buf, bufSize) < 0)
		return SJME_ERROR_AUDIO_WRITE_FAILED;

	/* Resume playback (if supported by the driver). */
	trigger = PCM_ENABLE_OUTPUT;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);
	ioctl(fd, SNDCTL_DSP_POST, NULL);

	/* Return the rendering error, if any. */
	return renderError;
}
