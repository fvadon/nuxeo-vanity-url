/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     <a href="mailto:fvadon@nuxeo.com">Fred Vadon</a>
 *
 *
 */

package org.nuxeo.labs.vanityurl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentLocationImpl;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.labs.vanityurl.GoCodec;

/**
 * @author <a href="mailto:fvadon@nuxeo.com">Fred Vadon</a>
 */
public class TestGoCodec {

    @Test
    public void testGetUrlFromDocumentView() {
        GoCodec codec = new GoCodec();
        DocumentLocation docLoc = new DocumentLocationImpl("demo", new IdRef("dbefd5a0-35ee-4ed2-a023-6817714f32cf"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("tabId", "TAB_CONTENT");
        DocumentView docView = new DocumentViewImpl(docLoc, "view_documents", params);

        String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf/view_documents?tabId=TAB_CONTENT";
        assertEquals(url, codec.getUrlFromDocumentView(docView));
    }

    @Test
    public void testGetDocumentViewFromUrl() {
        GoCodec codec = new GoCodec();
        String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf/view_documents?tabId=TAB_CONTENT";
        DocumentView docView = codec.getDocumentViewFromUrl(url);

        DocumentLocation docLoc = docView.getDocumentLocation();
        assertEquals("demo", docLoc.getServerName());
        assertEquals(new IdRef("dbefd5a0-35ee-4ed2-a023-6817714f32cf"), docLoc.getDocRef());
        assertEquals("view_documents", docView.getViewId());
        assertNull(docView.getSubURI());

        Map<String, String> params = docView.getParameters();
        assertEquals("TAB_CONTENT", params.get("tabId"));
    }

    // do the same without view id (optional)
    @Test
    public void testGetDocumentViewFromUrlNoViewId() {
        GoCodec codec = new GoCodec();
        String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf?tabId=TAB_CONTENT";
        DocumentView docView = codec.getDocumentViewFromUrl(url);

        DocumentLocation docLoc = docView.getDocumentLocation();
        assertEquals("demo", docLoc.getServerName());
        assertEquals(new IdRef("dbefd5a0-35ee-4ed2-a023-6817714f32cf"), docLoc.getDocRef());
        assertNull(docView.getViewId());
        assertNull(docView.getSubURI());

        Map<String, String> params = docView.getParameters();
        assertEquals("TAB_CONTENT", params.get("tabId"));
    }

    // test urls wit a sub uri do not match
    @Test
    public void testGetDocumentViewFromUrlWithSubUri() {
        GoCodec codec = new GoCodec();
        String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf/view_documents/whatever?tabId=TAB_CONTENT";
        DocumentView docView = codec.getDocumentViewFromUrl(url);
        assertNull(docView);
    }

    @Test
    public void testGetDocumentViewFromUrlWithJSessionId() {
        GoCodec codec = new GoCodec();
        String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf/view_documents;jsessionid=7CD6F2222BB08134A57BD2098DA16B2C.nuxeo?tabId=TAB_CONTENT";
        DocumentView docView = codec.getDocumentViewFromUrl(url);
        assertNotNull(docView);

        DocumentLocation docLoc = docView.getDocumentLocation();
        assertEquals("demo", docLoc.getServerName());
        assertEquals("view_documents", docView.getViewId());
        Map<String, String> params = docView.getParameters();
        assertEquals("TAB_CONTENT", params.get("tabId"));
    }

}
