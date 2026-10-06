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

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;

/**
 * Analyzer coverage probe for the AEM runtime risk query rules. Expected results:
 * INDEXED_SQL2 is covered by /oak:index/wkndCoverageProbeProperty (no finding);
 * UNINDEXED_XPATH and the QueryBuilder map are unindexed; MALFORMED_SQL2 is invalid.
 * Never called at runtime.
 */
public final class QueryCoverageProbe {

    static final String INDEXED_SQL2 =
            "SELECT * FROM [nt:base] WHERE [wkndCoverageProbe] = 'probe'";
    static final String UNINDEXED_XPATH =
            "/jcr:root/content/wknd//*[@wkndCoverageXpathUnindexed = 'probe']";
    static final String MALFORMED_SQL2 =
            "SELECT * FROM [nt:base WHERE [wkndCoverageProbe] = 'probe'";

    private QueryCoverageProbe() {
    }

    public static void runIndexed(Session session) throws RepositoryException {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        queryManager.createQuery(INDEXED_SQL2, Query.JCR_SQL2).execute();
    }

    @SuppressWarnings("deprecation")
    public static void runUnindexedXpath(Session session) throws RepositoryException {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        queryManager.createQuery(UNINDEXED_XPATH, Query.XPATH).execute();
    }

    public static void runMalformed(Session session) throws RepositoryException {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        queryManager.createQuery(MALFORMED_SQL2, Query.JCR_SQL2).execute();
    }

    public static void runUnindexedQueryBuilder(QueryBuilder queryBuilder, Session session) {
        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", "/content/wknd");
        predicates.put("property", "wkndCoverageQbUnindexed");
        predicates.put("property.value", "probe");
        queryBuilder.createQuery(PredicateGroup.create(predicates), session).getResult();
    }
}
