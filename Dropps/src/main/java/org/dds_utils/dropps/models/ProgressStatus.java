/*
    Copyright (C) 2008  Helmut Juskewycz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.dds_utils.dropps.models;

import java.io.File;

public class ProgressStatus {
    private boolean error;
    public File file;
    private String  message;
    private int     processable;
    private int     processed;

    public ProgressStatus(File file, int processed, int processable, String message) {
        this(file, processed, processable, message, false);
    }

    public ProgressStatus(File file, int processed, int processable, String message, boolean error) {
        this.processed   = processed;
        this.processable = processable;
        this.message     = message;
        this.error       = error;
        this.file        = file;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Number of processable elements.
     * @return
     */
    public int getProcessable() {
        return processable;
    }

    /**
     * Set number of processable elements.
     * @param processable
     */
    public void setProcessable(int processable) {
        this.processable = processable;
    }

    /**
     * number of processed ie finished elemeent
     * @return
     */
    public int getProcessed() {
        return processed;
    }

    /**
     * Set number of processed elements.
     * @param processed
     */
    public void setProcessed(int processed) {
        this.processed = processed;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
