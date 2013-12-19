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

package ru.jts.common.parser;

/**
 * @author : Camelion
 * @date : 18.08.12  1:12
 */
public abstract class AbstractParser<H extends AbstractHolder> {
    private final H holder;
    private String currentFile;

    protected AbstractParser(H holder) {
        this.holder = holder;
    }

    protected abstract void parse() throws Exception;

    protected H getHolder() {
        return holder;
    }

    public void load() throws Exception {
        getHolder().beforeParsing();
        parse();
        getHolder().afterParsing();
        getHolder().logAfterLoading();
        afterParsing();
    }

    protected void afterParsing() {

    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}
