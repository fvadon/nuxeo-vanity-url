package org.nuxeo.labs.vanityurl;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * This Seam bean manages the external references.
 *
 * @author <a href="mailto:fvadon@nuxeo.com">Fred Vadon</a>
 */
@Name("vanityUrl")
@Scope(ScopeType.CONVERSATION)
public class VanityUrlActionBean implements Serializable {


    public Integer setVanityURL(String documentUID, String vanityPart) {
        return VanityUrlActionHelper.setVanityURL(documentUID, vanityPart);
    }

    public void removeVanityURL(String documentUID) {
        VanityUrlActionHelper.removeVanityURL(documentUID);
    }

    public String getExistingDocIdForVanityURL(String vanityPart) {
        return VanityUrlActionHelper.getExistingDocIdForVanityURL(vanityPart);
    }

    public String getVanityURLForDocumentID(String documentUID) {
       return VanityUrlActionHelper.getVanityURLForDocumentID(documentUID);
    }


    private static final long serialVersionUID = 1L;

}
