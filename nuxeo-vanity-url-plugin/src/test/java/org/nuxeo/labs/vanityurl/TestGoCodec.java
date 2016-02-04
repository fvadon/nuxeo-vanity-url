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

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentLocationImpl;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.labs.vanityurl.GoCodec;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * @author <a href="mailto:fvadon@nuxeo.com">Fred Vadon</a>
 */
@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class})
@Deploy({"org.nuxeo.labs.vanityurl"})
public class TestGoCodec {

    private String vanityPart = "go-here";
    private String docId="dbefd5a0-35ee-4ed2-a023-6817714f32cf";
    private String url = "go/go-here";

    @Test
    public void testGetUrlFromDocumentView() {
        GoCodec codec = new GoCodec();
        DocumentLocation docLoc = new DocumentLocationImpl("demo", new IdRef(docId));

        assertTrue("Should get 1 if the vanityPart was added", VanityUrlActionHelper.setVanityURL(docId,vanityPart)==1);
        DocumentView docView = new DocumentViewImpl(docLoc, "view_documents");

        assertEquals(url, codec.getUrlFromDocumentView(docView));
        VanityUrlActionHelper.removeVanityURL(docId);

    }

    @Test
    public void testGetDocumentViewFromUrl() {
        GoCodec codec = new GoCodec();
        //String url = "go/demo/dbefd5a0-35ee-4ed2-a023-6817714f32cf/view_documents?tabId=TAB_CONTENT";


        assertTrue("Should get 1 if the vanityPart was added", VanityUrlActionHelper.setVanityURL(docId,vanityPart)==1);

        DocumentView docView = codec.getDocumentViewFromUrl(url);

        DocumentLocation docLoc = docView.getDocumentLocation();
        assertEquals("default", docLoc.getServerName());
        assertEquals(new IdRef(docId), docLoc.getDocRef());
        assertEquals("view_documents", docView.getViewId());
        assertNull(docView.getSubURI());

        VanityUrlActionHelper.removeVanityURL(docId);
    }

}
