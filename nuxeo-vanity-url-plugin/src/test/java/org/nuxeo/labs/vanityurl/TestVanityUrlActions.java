package org.nuxeo.labs.vanityurl;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.operations.FetchContextDocument;
import org.nuxeo.ecm.automation.core.operations.document.PublishDocument;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.labs.vanityurl.AbstractVanityUrlActions;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;


/**
 * @author fvadon
 */
@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class,
        EmbeddedAutomationServerFeature.class })
@Deploy({ "org.nuxeo.labs.vanityurl" })
public class TestVanityUrlActions extends AbstractVanityUrlActions {

    @Inject
    CoreSession session;

    @Inject
    AutomationService service;

    @Inject
    RepositoryManager rm;

    protected DocumentModel folder;

    protected DocumentModel section;

    protected DocumentModel docToPublish;

    protected DocumentModel publishedDoc;

    @Before
    public void initRepo() throws Exception {
        session.removeChildren(session.getRootDocument().getRef());
        session.save();

        folder = session.createDocumentModel("/", "Folder", "Folder");
        folder.setPropertyValue("dc:title", "Folder");
        folder = session.createDocument(folder);
        session.save();
        folder = session.getDocument(folder.getRef());

        section = session.createDocumentModel("/", "Section", "Section");
        section.setPropertyValue("dc:title", "Section");
        section = session.createDocument(section);
        session.save();
        section = session.getDocument(section.getRef());

        docToPublish = session.createDocumentModel("/Folder", "docToPublish",
                "File");
        docToPublish.setPropertyValue("dc:title", "File");
        docToPublish = session.createDocument(docToPublish);
        session.save();
        docToPublish = session.getDocument(docToPublish.getRef());
    }

    @Test
    public void vanityURLDirectoryTest() {
        DirectoryService dirService = Framework.getLocalService(DirectoryService.class);
        Session dirSession = dirService.open(VanityUrlConstant.VANITY_URL_DIRECTORY);
        assertEquals(dirSession.getIdField(),VanityUrlConstant.VANITY_URL_VANITY_FIELD);
        dirSession.close();
    }

    @Test
    public void vanityUrlActionTest() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        ctx.setInput(docToPublish);
        OperationChain initChain = new OperationChain("testpublish");
        initChain.add(FetchContextDocument.ID);
        initChain.add(PublishDocument.ID).set("target", section.getId());
        publishedDoc = (DocumentModel) service.run(ctx, initChain);
        assertNotNull(publishedDoc);
    }



}
