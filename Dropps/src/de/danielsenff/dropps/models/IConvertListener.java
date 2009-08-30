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
package de.danielsenff.dropps.models;

import java.io.File;

/**
 * A ConvertListener observes the convertion process. The according methods will be
 * called from the observerable converter.
 *
 */
public interface IConvertListener {

    /**
     * This method will be called <strong>before</strong> the file will be
     * splitted.
     * 
     * @param originalFile The original (large) file.
     */
    public void convertBegin(File originalFile);

    /**
     * This method will be called <strong>after</strong> the file is splitted.
     * 
     * @param originalFile The original (large) file.
     */
    public void convertEnd(File originalFile);
}
