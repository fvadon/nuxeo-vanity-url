/**
 *
 */

package org.nuxeo.labs.vanityurl;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.vanityurl.VanityUrlActionHelper;

/**
 * @author fvadon
 */
@Operation(id=RemoveVanityURL.ID, category=Constants.CAT_DOCUMENT, label="Remove Vanity URL for input document", description="")
public class RemoveVanityURL extends VanityUrlActionHelper {

    public static final String ID = "RemoveVanityURL";

    @OperationMethod
    public DocumentModel run(DocumentModel input) {
        removeVanityURL(input.getId());
      return input;
    }

}
