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

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.jts.common.parser.AbstractHolder;
import ru.jts.common.parser.AbstractParser;

import java.io.InputStream;

/**
 * @author : Camelion
 * @date : 18.08.12  1:21
 */
/* package */ abstract class AbstractXMLParser<H extends AbstractHolder> extends AbstractParser<H> {
    /* package */ final SAXReader reader;


    protected AbstractXMLParser(H holder) {
        super(holder);
        reader = new SAXReader();
        reader.setValidation(true);
    }

    protected abstract void parseFile(InputStream f, String fileName) throws Exception;

    protected abstract void readData(Element rootElement);
}
