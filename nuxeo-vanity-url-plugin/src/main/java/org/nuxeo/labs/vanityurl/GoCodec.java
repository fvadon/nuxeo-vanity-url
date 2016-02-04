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
 *     Nuxeo - initial API and implementation
 *
 * $Id: DocumentIdCodec.java 22535 2007-07-13 14:57:58Z atchertchian $
 */

package org.nuxeo.labs.vanityurl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentLocationImpl;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.service.AbstractDocumentViewCodec;

/**
 * Codec handling a document repository, id, view and additional request parameters.
 *
 * @author Fred Vadon
 */
public class GoCodec extends AbstractDocumentViewCodec {

    public static final String PREFIX = "go";

    private static final String VIEW_ID = "view_documents";

    private static final String DEFAULT_SERVER = "default";

    // nxdoc/server/docId/view_id/?requestParams
    // public static final String URLPattern = "/(\\w+)/([a-zA-Z_0-9\\-]+)(/([a-zA-Z_0-9\\-\\.;=]*))?(/)?(\\?(.*)?)?";
    public static final String URLPattern = "/([a-zA-Z_0-9\\-]+)";

    public GoCodec() {
    }

    public GoCodec(String prefix) {
    }

    @Override
    public String getPrefix() {
        if (prefix != null) {
            return prefix;
        }
        return PREFIX;
    }

    @Override
    public String getUrlFromDocumentView(DocumentView docView) {
        DocumentLocation docLoc = docView.getDocumentLocation();
        if (docLoc != null) {
            IdRef docRef = docLoc.getIdRef();
            if (docRef == null) {
                return null;
            }
            String vanityPart = VanityUrlActionHelper.getVanityURLForDocumentID(docRef.toString());
            if(vanityPart.length()>0){
                String uri = getPrefix()+"/"+vanityPart;
                return uri;
            }
        }
        return null;

    }

    /**
     * Extracts document location from a Zope-like URL ie: server/path_or_docId/view_id/tab_id .
     */
    @Override
    public DocumentView getDocumentViewFromUrl(String url) {
        final Pattern pattern = Pattern.compile(getPrefix() + URLPattern);
        Matcher m = pattern.matcher(url);
        if (m.matches()) {
            if (m.groupCount() == 1) {

                // for debug
                // for (int i = 1; i < m.groupCount() + 1; i++) {
                // System.err.println(i + ": " + m.group(i));
                // }

                // Hardcoding the serveur id
                // final String server = m.group(1);
                final String server = DEFAULT_SERVER;
                String vanityPart = m.group(1);
                String uuid = VanityUrlActionHelper.getExistingDocIdForVanityURL(vanityPart);
                if (uuid.length() > 0) {
                    final DocumentRef docRef = new IdRef(uuid);
                    String viewId = VIEW_ID;

                    final DocumentLocation docLoc = new DocumentLocationImpl(server, docRef);

                    return new DocumentViewImpl(docLoc, viewId);
                }
            }
        }

        return null;
    }

}
