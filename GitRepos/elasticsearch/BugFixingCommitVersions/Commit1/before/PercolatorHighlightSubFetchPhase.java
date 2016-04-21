/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.percolator;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.ReaderUtil;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.query.PercolateQuery;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.fetch.FetchSubPhase;
import org.elasticsearch.search.highlight.HighlightPhase;
import org.elasticsearch.search.highlight.SearchContextHighlight;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.internal.SubSearchContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// Highlighting in the case of the percolate query is a bit different, because the PercolateQuery itself doesn't get highlighted,
// but the source of the PercolateQuery gets highlighted by each hit with type '.percolator' (percolator queries).
public class PercolatorHighlightSubFetchPhase implements FetchSubPhase {

    private final HighlightPhase highlightPhase;

    @Inject
    public PercolatorHighlightSubFetchPhase(HighlightPhase highlightPhase) {
        this.highlightPhase = highlightPhase;
    }

    @Override
    public boolean hitsExecutionNeeded(SearchContext context) {
        return context.highlight() != null && locatePercolatorQuery(context.query()) != null;
    }

    @Override
    public void hitsExecute(SearchContext context, InternalSearchHit[] hits) {
        PercolateQuery percolateQuery = locatePercolatorQuery(context.query());
        if (percolateQuery == null) {
            // shouldn't happen as we checked for the existence of a percolator query in hitsExecutionNeeded(...)
            throw new IllegalStateException("couldn't locate percolator query");
        }

        List<LeafReaderContext> ctxs = context.searcher().getIndexReader().leaves();
        PercolatorQueryCache queriesRegistry = context.percolatorQueryCache();
        IndexSearcher percolatorIndexSearcher = percolateQuery.getPercolatorIndexSearcher();

        LeafReaderContext percolatorLeafReaderContext = percolatorIndexSearcher.getIndexReader().leaves().get(0);
        FetchSubPhase.HitContext hitContext = new FetchSubPhase.HitContext();
        SubSearchContext subSearchContext =
                createSubSearchContext(context, percolatorLeafReaderContext, percolateQuery.getDocumentSource());

        for (InternalSearchHit hit : hits) {
            LeafReaderContext ctx = ctxs.get(ReaderUtil.subIndex(hit.docId(), ctxs));
            int segmentDocId = hit.docId() - ctx.docBase;
            Query query = queriesRegistry.getQueries(ctx).getQuery(segmentDocId);
            if (query != null) {
                subSearchContext.parsedQuery(new ParsedQuery(query));
                hitContext.reset(
                        new InternalSearchHit(0, "unknown", new Text(percolateQuery.getDocumentType()), Collections.emptyMap()),
                        percolatorLeafReaderContext, 0, percolatorIndexSearcher
                );
                hitContext.cache().clear();
                highlightPhase.hitExecute(subSearchContext, hitContext);
                hit.highlightFields().putAll(hitContext.hit().getHighlightFields());
            }
        }
    }

    @Override
    public Map<String, ? extends SearchParseElement> parseElements() {
        return Collections.emptyMap();
    }

    @Override
    public boolean hitExecutionNeeded(SearchContext context) {
        return false;
    }

    @Override
    public void hitExecute(SearchContext context, HitContext hitContext) {
    }

    static PercolateQuery locatePercolatorQuery(Query query) {
        if (query instanceof PercolateQuery) {
            return (PercolateQuery) query;
        } else if (query instanceof BooleanQuery) {
            for (BooleanClause clause : ((BooleanQuery) query).clauses()) {
                PercolateQuery result = locatePercolatorQuery(clause.getQuery());
                if (result != null) {
                    return result;
                }
            }
        } else if (query instanceof ConstantScoreQuery) {
            return locatePercolatorQuery(((ConstantScoreQuery) query).getQuery());
        } else if (query instanceof BoostQuery) {
            return locatePercolatorQuery(((BoostQuery) query).getQuery());
        }

        return null;
    }

    private SubSearchContext createSubSearchContext(SearchContext context, LeafReaderContext leafReaderContext, BytesReference source) {
        SubSearchContext subSearchContext = new SubSearchContext(context);
        subSearchContext.highlight(new SearchContextHighlight(context.highlight().fields()));
        // Enforce highlighting by source, because MemoryIndex doesn't support stored fields.
        subSearchContext.highlight().globalForceSource(true);
        subSearchContext.lookup().source().setSegmentAndDocument(leafReaderContext, 0);
        subSearchContext.lookup().source().setSource(source);
        return subSearchContext;
    }
}
