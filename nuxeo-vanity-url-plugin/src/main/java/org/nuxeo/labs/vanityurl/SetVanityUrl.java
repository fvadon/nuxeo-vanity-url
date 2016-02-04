/**
 *
 */

package org.nuxeo.labs.vanityurl;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.vanityurl.VanityUrlActionHelper;

/**
 * @author fvadon
 */
@Operation(id=SetVanityUrl.ID, category=Constants.CAT_DOCUMENT, label="Set Vanity Url", description="Stores the VanityPart for the given documentUID, "
        + "vanityPart must be unique. "
        + "A document only has one vanityURL."
        + "Put in setVanityResult context var the result of the action/n:"
        + "1 is the vanityURL was added, -1 if the vanityPart wasn't added because does not meet criteria,"
        + "-2 if the vanityURL already exists for another document")
public class SetVanityUrl extends VanityUrlActionHelper {

    public static final String ID = "SetVanityUrl";

    @Context
    protected OperationContext ctx;

    @Param(name = "Vanity Part", required = true)
    protected String vanityPart;

    @OperationMethod
    public DocumentModel run(DocumentModel input) {
        ctx.put("setVanityResult",setVanityURL(input.getId(), vanityPart));
      return input;
    }

}
