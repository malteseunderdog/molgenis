package org.molgenis.data.excel;

import javax.annotation.PostConstruct;

import org.molgenis.data.FileRepositoryCollectionFactory;
import org.molgenis.data.support.GenericImporterExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelDataConfig
{
	@Autowired
	private FileRepositoryCollectionFactory fileRepositoryCollectionFactory;

	/**
	 * Registers the ExcelRepositorySource factory so it can be used by DataService.createFileRepositorySource(File
	 * file);
	 */
	@PostConstruct
	public void registerExcelRepositorySource()
	{
		fileRepositoryCollectionFactory.addFileRepositoryCollectionClass(ExcelRepositoryCollection.class,
				GenericImporterExtensions.getExcel());
	}

}
