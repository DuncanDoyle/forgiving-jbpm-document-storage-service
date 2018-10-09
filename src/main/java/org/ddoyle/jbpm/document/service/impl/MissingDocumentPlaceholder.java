package org.ddoyle.jbpm.document.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jbpm.document.Document;
import org.jbpm.document.service.impl.DocumentImpl;

/**
 * Placeholder for a missing {@link Document}. Since an instance of this class should only be used in a serious error condition (the
 * {@link Document} to be unmarshalled is missing), every operation other than getting or setting the identifier of this document is
 * unsupported and will throw an {@link UnsupportedOperationException}.
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class MissingDocumentPlaceholder extends DocumentImpl {

	public MissingDocumentPlaceholder(String identifier) {
		// Setting default values for identifier && download link
		setIdentifier(identifier);
	}

	// We don't allow any modification on the missing document other than the document's identifier.

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting the name of a missing document is not supported.");
	}

	@Override
	public void setSize(long size) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting the size of a missing document is not supported.");
	}

	@Override
	public void setContent(byte[] content) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting the content of a missing document is not supported.");
	}

	@Override
	public File toFile() throws IOException {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Converting a missing document to a file is not supported.");
	}

	@Override
	public void setLink(String link) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting a link to a non-existing document is not supported.");
	}

	@Override
	public void setLastModified(Date lastModified) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting a last modification date on a missing document is not supported.");
	}

	@Override
	public void setAttributes(Map<String, String> attributes) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Setting attributes on a missing document is not supported.");
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the name of a missing document is not supported.");
	}

	@Override
	public String getLink() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the link of a missing document is not supported.");
	}

	@Override
	public long getSize() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the size of a missing document is not supported.");
	}

	@Override
	public Date getLastModified() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the last modification date of a missing document is not supported.");
	}

	@Override
	public String getAttribute(String attributeName) {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting an attribute of a missing document is not supported.");
	}

	@Override
	public Map<String, String> getAttributes() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the attributes of a missing document is not supported.");
	}

	@Override
	public byte[] getContent() {
		throw new UnsupportedOperationException(
				"This is a placeholder for a missing document. Getting the content of a missing document is not supported.");
	}

}
