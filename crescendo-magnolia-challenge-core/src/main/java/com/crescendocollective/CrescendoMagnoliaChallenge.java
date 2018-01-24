package com.crescendocollective;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;
import info.magnolia.module.blossom.module.BlossomModuleSupport;
import com.crescendocollective.config.CrescendoMagnoliaChallengeConfiguration;
import com.crescendocollective.config.BlossomServletConfiguration;

/**
 * This class manages the lifecycle of the crescendo-magnolia-challenge-core module. It starts and stops Spring when Magnolia starts up and
 * shuts down. The dispatcher servlet we create here is a servlet but its managed internally and never exposed to the
 * outside world. A request will never reach this servlet directly. It is only accessed by Magnolia to render the
 * templates, areas and components and display the dialogs managed by the servlet.
 */
public class CrescendoMagnoliaChallenge extends BlossomModuleSupport implements ModuleLifecycle {

    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        if (moduleLifecycleContext.getPhase() == ModuleLifecycleContext.PHASE_SYSTEM_STARTUP) {
            super.initRootWebApplicationContext(CrescendoMagnoliaChallengeConfiguration.class);
            super.initBlossomDispatcherServlet("blossom", BlossomServletConfiguration.class);
        }
    }

    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
        if (moduleLifecycleContext.getPhase() == ModuleLifecycleContext.PHASE_SYSTEM_SHUTDOWN) {
            super.destroyDispatcherServlets();
            super.closeRootWebApplicationContext();
        }
    }
}
