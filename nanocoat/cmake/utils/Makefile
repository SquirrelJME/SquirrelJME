# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities (backwards compatible make)

SJME_CFLAGS=-I../../include/

$(OUTPUT_DIR)/decode$(HOST_EXE_SUFFIX):		decode.c
								$(CC) -o "$@" $(SJME_CFLAGS) $<

$(OUTPUT_DIR)/simple$(HOST_EXE_SUFFIX):		simple.c
								$(CC) -o "$@" $(SJME_CFLAGS) $<

$(OUTPUT_DIR)/sourceize$(HOST_EXE_SUFFIX):	sourceize.c
								$(CC) -o "$@" $(SJME_CFLAGS) $<

$(OUTPUT_DIR)/platform$(HOST_EXE_SUFFIX):	platform.c
								$(CC) -o "$@" $(SJME_CFLAGS) $<

$(OUTPUT_DIR)/$(HOST_DYLIB_PREFIX)jawt$(HOST_DYLIB_SUFFIX):	stub_jawt.c
								$(CC) -o "$@" $(SJME_CFLAGS) $< -shared -fPIC

$(OUTPUT_DIR)/$(HOST_DYLIB_PREFIX)jvm$(HOST_DYLIB_SUFFIX):	stub_jvm.c
								$(CC) -o "$@" $(SJME_CFLAGS) $< -shared -fPIC


all:							$(OUTPUT_DIR)/decode$(HOST_EXE_SUFFIX) \
	$(OUTPUT_DIR)/simple$(HOST_EXE_SUFFIX) \
	$(OUTPUT_DIR)/sourceize$(HOST_EXE_SUFFIX) \
	$(OUTPUT_DIR)/platform$(HOST_EXE_SUFFIX) \
	$(OUTPUT_DIR)/$(HOST_DYLIB_PREFIX)jawt$(HOST_DYLIB_SUFFIX) \
	$(OUTPUT_DIR)/$(HOST_DYLIB_PREFIX)jvm$(HOST_DYLIB_SUFFIX)

clean:
								rm -f "$(OUTPUT_DIR)/./decode$(HOST_EXE_SUFFIX)" \
	"$(OUTPUT_DIR)/./simple$(HOST_EXE_SUFFIX)" \
	"$(OUTPUT_DIR)/./sourceize$(HOST_EXE_SUFFIX)" \
	"$(OUTPUT_DIR)/./platform$(HOST_EXE_SUFFIX)" \
	"$(OUTPUT_DIR)/./$(HOST_DYLIB_PREFIX)jawt$(HOST_DYLIB_SUFFIX)" \
	"$(OUTPUT_DIR)/./$(HOST_DYLIB_PREFIX)jvm$(HOST_DYLIB_SUFFIX)"

.PHONY:							all
