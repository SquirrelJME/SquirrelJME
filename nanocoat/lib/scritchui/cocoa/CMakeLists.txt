# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUICocoa SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
	cocoaComponent.m
	cocoaContainer.m
	cocoaCore.m
	cocoaIntern.m
	cocoaLabel.m
	cocoaLaf.m
	cocoaLoop.m
	cocoaMenu.m
	cocoaPanel.m
	cocoaPencil.m
	cocoaScreen.m
	cocoaScrollPanel.m
	cocoaView.m
	cocoaWindow.m)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUICocoa
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUICocoa PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUICocoa
	"squirreljme-scritchui-cocoa")

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(ScritchUICocoa
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

target_link_libraries(ScritchUICocoa
	"-framework Foundation"
	"-framework Cocoa")
