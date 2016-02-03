package org.nuxeo.labs.vanityurl;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;

public abstract class AbstractVanityUrlActions {

    /**
     * Store the VanityPart for the given documentUID, vanityPart must be unique. A document only has one vanityURL
     *
     * @param documentUID
     * @param vanityPart
     * @return 1 is the vanityURL was added, -1 if the vanityPart wasn't added because does not meet criteria -2 if the
     *         vanityURL already exists for another document,
     */
    public Integer setVanityURL(String documentUID, String vanityPart) {
        if (!vanityUrlMeetsCriteria(vanityPart)) {
            return -1;
        }
        String previousInfo = getExistingDocIdForVanityURL(vanityPart);
        if (previousInfo.equals(documentUID)) {
            // Vanity URL is here for the current document already
            return 1;
        } else if (previousInfo.length() > 0) {
            // another Doc is associated
            return -2;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(VanityUrlConstant.VANITY_URL_DOCUMENT_FIELD, documentUID);
        map.put(VanityUrlConstant.VANITY_URL_VANITY_FIELD, vanityPart);

        DirectoryService dirService = Framework.getLocalService(DirectoryService.class);
        Session dirSession = dirService.open(VanityUrlConstant.VANITY_URL_DIRECTORY);
        try {
            dirSession.createEntry(map);
        } finally {
            dirSession.close();
        }

        return 1;
    }

    /**
     * Remove the VanityURL for the given documentUID
     *
     * @param documentUID
     */
    public void removeVanityURL(String documentUID) {

    }

    /**
     * Check if the vanityPart already exists and get the associated documentID.
     *
     * @param documentUID
     * @param vanityPart
     * @return String of the associated documentUID if exists, empty String if does not exists.
     */
    public String getExistingDocIdForVanityURL(String vanityPart) {
        DirectoryService dirService = Framework.getLocalService(DirectoryService.class);
        Session dirSession = dirService.open(VanityUrlConstant.VANITY_URL_DIRECTORY);
        try {
            DocumentModel directoryEntry = dirSession.getEntry(vanityPart);
            if (directoryEntry == null) {
                return "";
            }
            return (String) directoryEntry.getPropertyValue(VanityUrlConstant.VANITY_URL_DOCUMENT_FIELD);
        } finally {
            dirSession.close();
        }
    }

    /**
     * Check if the vanityPart can be used as a url part, only checks it's not empty for now
     *
     * @param vanityPart
     * @return
     */
    public Boolean vanityUrlMeetsCriteria(String vanityPart) {

        if (vanityPart.length() == 0) {
            return false;
        }
        return true;

    }

}
