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

sjme_errorCode sjme_scritchaudio_oss_loopIterate(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream)
{
	sjme_errorCode rendError, playError;
	int fd, trigger;
	sjme_jint i, wc, n;
	sjme_scritchaudio_source source;
	sjme_list(sjme_scritchaudio_source)* sources;
	sjme_scritchaudio_renderInfo* renderInfo;
	sjme_scritchaudio_streamBuffer* rend;
	sjme_scritchaudio_streamBuffer* play;
	
	if (inState == NULL || inStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

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

	/* Recover the render info. */
	renderInfo = &inStream->data.renderInfo;

	/* Which buffer are we playing and which are we rendering? */
	rend = &inStream->data.buffers[inStream->data.renderBuffer];
	play = &inStream->data.buffers[!inStream->data.renderBuffer];

	/* Reset error states. */
	playError = SJME_ERROR_NONE;
	rendError = SJME_ERROR_NONE;

	/* Disable playback (if supported by the driver). */
	trigger = 0;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);

	/* Send the data to the sound card, pre-rendering, write may fail. */
	for (i = 0, n = renderInfo->bufSize; i < n;)
	{
		/* Send as much as possible to the sound card. */
		wc = write(fd, SJME_POINTER_OFFSET(play->buffer, i),
			renderInfo->bufSize - i);
		if (wc < 0)
			break;
		
		/* Shift buffer up. */
		i += wc;
	}
	
	/* While the sound card is playing audio, render the next batch of */
	/* audio into the buffer. This will happen if playback is synchronous. */
	rendError = source->renderFunc(inState, source, renderInfo,
		(sjme_scritchaudio_buffer*)rend->buffer);
	
	/* Resume playback (if supported by the driver). */
	trigger = PCM_ENABLE_OUTPUT;
	ioctl(fd, SNDCTL_DSP_SETTRIGGER, &trigger);
	ioctl(fd, SNDCTL_DSP_POST, NULL);
	
	/* Send the rest of the data to the sound card post-rendering. */
	while (i < n)
	{
		/* Send as much as possible to the sound card. */
		wc = write(fd, SJME_POINTER_OFFSET(play->buffer, i),
			renderInfo->bufSize - i);
		if (wc < 0)
			break;
		
		/* Shift buffer up. */
		i += wc;
	}

	/* Ladder any errors. */
	if (sjme_error_is(rendError))
		return sjme_error_default(rendError);
	if (sjme_error_is(playError))
		return sjme_error_default(playError);
	return SJME_ERROR_NONE;
}
