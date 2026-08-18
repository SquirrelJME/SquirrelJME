# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities (backwards compatible make)

SJME_CFLAGS=-I../ -I../../../include/ -DSJME_CONFIG_HAS_NO_C11_THREADS=1

$(OUTPUT_DIR)/sourceize$(HOST_EXE_SUFFIX):		sourceize.c
												$(CC) -o "$@" $(SJME_CFLAGS) $<

all:					$(OUTPUT_DIR)/sourceize$(HOST_EXE_SUFFIX)

clean:
						rm -f "$(OUTPUT_DIR)/./sourceize$(HOST_EXE_SUFFIX)"

.PHONY:							all
