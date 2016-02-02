package org.nuxeo.labs.vanityurl;

public abstract class AbstractVanityUrlActions {

    /**
    * Store the VanityPart for the given documentUID, vanityPart must be unique. A document only has one vanityURL
    * @param documentUID
    * @param vanityPart
    * @return 1 is the vanityURL was added, 2 if the vaintyURL was added replacing a previous value -1 if the vanityPart does not meet criteria (to be defined)
    * -2 of the vanityURL already exists,
    */
    public Integer addVanityURL(String documentUID, String vanityPart) {
        return 0;
    }

    /**
     * Remove the VanityURL for the given documentUID
     * @param documentUID
     */
    public void removeVanityURL(String documentUID) {

    }


    /**
     * Check if the vanityPart already exists.
     * @param documentUID
     * @param vanityPart
     * @return 1 if exists, 0 if does not exists.
     */
    public Integer ExistsVanityURL(String documentUID, String vanityPart) {
        return 0;
    }


}
