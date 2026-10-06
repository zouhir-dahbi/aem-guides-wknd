/*
 *  Copyright 2026 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.coverage;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

/**
 * Test-only analyzer coverage probe: resolver leak shapes beyond the single-method case, to measure CQRules CQBP-72 coverage.
 */
public final class ResourceResolverLeakVariantsProbe {

    private final ResourceResolverFactory resolverFactory;
    private ResourceResolver heldResolver;

    public ResourceResolverLeakVariantsProbe(ResourceResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
    }

    public String leakThroughHelper() throws LoginException {
        ResourceResolver resolver = openResolver();
        return resolver.getUserID();
    }

    public void leakIntoField() throws LoginException {
        heldResolver = resolverFactory.getServiceResourceResolver(null);
    }

    public String closeOnHappyPathOnly() throws LoginException {
        ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null);
        String userId = resolver.getUserID();
        resolver.close();
        return userId;
    }

    public Runnable leakInRunnable() {
        return () -> {
            try {
                ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null);
                resolver.getUserID();
            } catch (LoginException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public boolean hasHeldResolver() {
        return heldResolver != null;
    }

    private ResourceResolver openResolver() throws LoginException {
        return resolverFactory.getServiceResourceResolver(null);
    }
}
