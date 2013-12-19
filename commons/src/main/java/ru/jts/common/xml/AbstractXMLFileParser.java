/*
 * Copyright 2012 jts
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.jts.common.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import ru.jts.common.log.Log;
import ru.jts.common.parser.AbstractHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author : Camelion
 * @date : 18.08.12  1:12
 */
public abstract class AbstractXMLFileParser<H extends AbstractHolder> extends AbstractXMLParser<H> {
    private static final String LOG_TAG = "AbstractXMLFileParser.java";

    protected AbstractXMLFileParser(H holder) {
        super(holder);
    }

    @Override
    protected final void parse() {
        File xmlFile = new File(getXMLFileName());
        if (!xmlFile.exists()) {
            Log.e(LOG_TAG, "XML file {} not exists!", getXMLFileName());
            return;
        }

        File dtdFile = new File(getDTDFileName());
        if (!dtdFile.exists()) {
            Log.e(LOG_TAG, "DTD file {} not exists!", getXMLFileName());
            return;
        }

        reader.setEntityResolver(new DTDEntityResolver(getDTDFileName()));
        try {
            parseFile(new FileInputStream(xmlFile), getXMLFileName());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception while parse file " + getXMLFileName(), e);
        }
    }

    @Override
    protected void parseFile(InputStream f, String fileName) throws DocumentException {
        setCurrentFile(fileName);

        Document document = reader.read(f);
        readData(document.getRootElement());
    }

    protected abstract String getXMLFileName();

    protected abstract String getDTDFileName();
}
