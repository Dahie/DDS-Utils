/*
 * TMFileModelSize.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package net.bouthier.treemapSwing.fileViewer;

import java.io.File;

import net.bouthier.treemapSwing.TMComputeSizeAdapter;


/**
 * The TMFileModelSize class implements an example of a TMComputeSizeAdapter
 * for a TMFileModelNode.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TMFileModelSize 
	extends TMComputeSizeAdapter {


    /* --- TMComputeSizeAdapter -- */

    public boolean isCompatibleWithObject(Object node) {
        if (node instanceof File) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the size of the node.
     */
    public float getSizeOfObject(Object node) {
        if (node instanceof File) {
            File file = (File) node;
            return file.length();
        }
        return 0.0f;
    }
}
