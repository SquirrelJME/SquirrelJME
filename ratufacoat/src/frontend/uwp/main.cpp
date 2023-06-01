/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <windows.h>
#include <winrt/Windows.ApplicationModel.Activation.h>
#include <winrt/Windows.UI.Xaml.h>

using namespace winrt;
using namespace winrt::Windows::ApplicationModel::Activation;
using namespace winrt::Windows::UI;
using namespace winrt::Windows::UI::Xaml;

/**
 * Main SquirrelJME application.
 * 
 * @since 2021/10/09
 */
struct SquirrelJMEApp : ApplicationT<SquirrelJMEApp>
{
	void OnLaunched(LaunchActivatedEventArgs const&)
	{
		Window window = Window::Current();
		
		window.Activate();
	}
};

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE, PWSTR pCmdLine,
	int nCmdShow)
{
	/* Everyone lives here!* */
	winrt::init_apartment();
	
	/* Start the application. */
	Application::Start([](auto &&){winrt::make<SquirrelJMEApp>();});
	
	/* And everyone is gone. */
	winrt::uninit_apartment();
}
