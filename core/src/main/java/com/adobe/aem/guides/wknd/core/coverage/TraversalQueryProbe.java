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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

/**
 * Test-only analyzer coverage probe: a JCR-SQL2 query on a property no index covers, which traverses at runtime.
 */
public final class TraversalQueryProbe {

    static final String UNINDEXED_QUERY =
            "SELECT * FROM [nt:base] WHERE [wkndCoverageUnindexed] = 'probe'";

    private TraversalQueryProbe() {
    }

    public static long countUnindexed(ResourceResolverFactory resolverFactory) throws LoginException, RepositoryException {
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(null)) {
            Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                return 0;
            }
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            return queryManager.createQuery(UNINDEXED_QUERY, Query.JCR_SQL2).execute().getNodes().getSize();
        }
    }
}
