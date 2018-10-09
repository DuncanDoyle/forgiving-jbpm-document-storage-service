package org.ddoyle.jbpm.document.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbpm.document.Document;
import org.jbpm.document.service.DocumentStorageService;
import org.jbpm.document.service.impl.DocumentImpl;
import org.jbpm.document.service.impl.DocumentStorageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DocumentStorageService} implementation that is more forgiving than the default {@link DocumentStorageServiceImpl}. If this
 * storage service tries to load a missing document, the {@link Document} that will be returned is a {@link MissingDocumentPlaceholder}.
 * This allows process marshalling and unmarshalling to succesfully complete, and allows interaction with process instance and/or case
 * instance. However, a document missing from the storage service is regarded as a severe problem and needs to be fixed ASAP. This can be
 * done by adding a new physical document with the given identifier of the missing document, or by adding a new {@link Document} instance to
 * the process instance and or case that replaces the {@link MissingDocumentPlaceholder}.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class ForgivingDocumentStorageServiceImpl extends DocumentStorageServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(ForgivingDocumentStorageServiceImpl.class);

	/**
	 * Saves the given {@link Document}.
	 * <p/>
	 * When the {@link Document} is of type {@link MissingDocumentPlaceholder}, we skip saving the file on disk (as the class represents a
	 * missing document}, but we will log an error. Having {@link MissingDocumentPlaceholder} instances as process variables or in a
	 * casefile is a serious error.
	 * 
	 */
	@Override
	public Document saveDocument(Document document, byte[] content) {

		if (document instanceof MissingDocumentPlaceholder) {
			log.error(
					"Trying to save a MissingDocumentPlaceholder!!! Having a MissingDocumentPlaceholder in your rules, processes and/or case instances is a severe error. Please fix this problem ASAP by creating a document with identtifier '"
							+ document.getIdentifier() + "'.");
			return document;
		}

		if (StringUtils.isEmpty(document.getIdentifier())) {
			document.setIdentifier(generateUniquePath());
		}

		File destination = getFileByPath(document.getIdentifier() + File.separator + document.getName());

		try {
			FileUtils.writeByteArrayToFile(destination, content);
			destination.getParentFile().setLastModified(document.getLastModified().getTime());
			destination.setLastModified(document.getLastModified().getTime());
		} catch (IOException e) {
			log.error("Error writing file {}: {}", document.getName(), e);
		}

		return document;
	}

	/**
	 * Loads the {@link Document} with the given identifier.
	 * <p/>
	 * NOTE: if the {@link Document} is missing, we will be forgiving and create and return {@link MissingDocumentPlaceholder}. This allows
	 * the process or case instance to be loaded on a missing document. Note however that this is a very serious error!
	 * 
	 * @param the
	 *            identifier of the {@link Document} to be loaded.
	 * 
	 * @return the {@link Document}.
	 */
	@Override
	public Document getDocument(String id) {
		File file = getFileByPath(id);

		if (file.exists() && !file.isFile() && !ArrayUtils.isEmpty(file.listFiles())) {
			try {
				File destination = file.listFiles()[0];
				DocumentImpl doc = new DocumentImpl(id, destination.getName(), destination.length(), new Date(destination.lastModified()));
				doc.setLoadService(this);
				return doc;
			} catch (Exception e) {
				log.error("Error loading document '{}': {}", id, e);
			}
		} else {
			/*
			 * Document is missing. Log an error (it's more serious than a warning, those get ignored most of the time). Nevertheless, we
			 * will be forgiving and return a MissingDocumentPlaceholder, so the Marshaller will continue. Note that this is a very serious
			 * error!!!
			 */
			log.error("The document with identifier '" + id
					+ "' is missing! This is a severe error! Replacing the document with a MissingDocumentPlaceholder. Please fix this problem ASAP by creating a document with identtifier: '"
					+ id + "'.");
			Document missingDocument = new MissingDocumentPlaceholder(id);
			return missingDocument;
		}

		return null;
	}

}
